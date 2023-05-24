package com.conifer.pipelinejobs.schedulers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class SchedulerConfig {

    @Bean("deviceMonitorRestTemplate")
    public RestTemplate deviceMonitorRestTemplate(){
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
    }
}
