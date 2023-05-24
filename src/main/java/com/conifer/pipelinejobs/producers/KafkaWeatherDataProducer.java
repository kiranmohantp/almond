package com.conifer.pipelinejobs.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaWeatherDataProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "weather_data";
    Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(cron = "0 0/15 00-23 * * *")
    public void fillDataToKafkaFromDevice() {
        try {
            String jsonResponseFromDevice = callDeviceForWeatherData();
            KafkaProducer<String, String> producer = createKafkaProducer();
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, jsonResponseFromDevice);
            producer.send(record).get();
            producer.close();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Unable to push data to kafka ", e);
        }

    }

    @NotNull
    private KafkaProducer<String, String> createKafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        return producer;
    }

    @Nullable
    private String callDeviceForWeatherData() {
        String endpoint = "http://192.168.1.12/json/summery";
        RestTemplate restTemplate = createRestTemplate();
        String response = restTemplate
                .getForEntity(endpoint, String.class)
                .getBody();
        return response;
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS)).build();
    }
}
