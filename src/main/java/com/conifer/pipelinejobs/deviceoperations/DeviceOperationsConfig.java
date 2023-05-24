package com.conifer.pipelinejobs.deviceoperations;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class DeviceOperationsConfig {
    @Bean("operationRestTemplate")
    public RestTemplate operationRestTemplate() {
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.of(15, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(15, ChronoUnit.SECONDS)).build();
    }
}
