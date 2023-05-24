package com.conifer.pipelinejobs.dbproducers;

import com.conifer.pipelinejobs.dbproducers.models.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;

@Service
public class KafkaToInFluxDataTransfer {
    String bucket = "weather_station";
    String org = "conifer";

    Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(cron = "0 0/15 00-23 * * *")
    public void pollKafkaAndSyncWithInfluxDb() {
        InfluxDBClient client = getInfluxDbClient();

        Properties properties = createConnectionProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("weather_data"));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            try {
                WeatherData data = parseDataAndAddTimeStamp(record);
                writeDataToInfluxDbBucket(client, data);
                LOGGER.info("Data saved to Influx db");
            } catch (JsonProcessingException e) {
                LOGGER.error("Uparsable json so skipping : " + record.value());
            }
            consumer.commitSync();
            LOGGER.info("Kafka offset committed.");
        }

    }

    private void writeDataToInfluxDbBucket(InfluxDBClient client, WeatherData data) {
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, WritePrecision.NS, data);
    }

    @NotNull
    private WeatherData parseDataAndAddTimeStamp(ConsumerRecord<String, String> record) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherData data = objectMapper.readValue(record.value(), WeatherData.class);
        data.setTime(Instant.now());
        data.setFrom("Java_Job");
        return data;
    }

    @NotNull
    private InfluxDBClient getInfluxDbClient() {
        return InfluxDBClientFactory.create("http://localhost:8086",
                "UEFA9sgTTbNrB6SzYntII4KJqE8b2FOAj6Om1LfLGrcdHey_QVI3c1cB2xvPzjzzwwwfrsuDrr-LVlNL1U6Z9A==".toCharArray());
    }

    @NotNull
    private static Properties createConnectionProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "db-sync-group");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }
}

