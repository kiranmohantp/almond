package com.conifer.pipelinejobs.dbproducers.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

@Measurement(name = "WeatherData")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherData {
    @Column(tag = true)
    String from;
    @Column(timestamp = true)
    Instant time;
    @Column
    Integer waterPercentage;
    @Column
    Float altitude;
    @Column
    Float seaLevelPressure;
    @Column
    Float atmHumidity;
    @Column
    Float atmTemp;
    @Column
    Float pressureInPa;
}