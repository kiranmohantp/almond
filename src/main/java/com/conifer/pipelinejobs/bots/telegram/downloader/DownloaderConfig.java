package com.conifer.pipelinejobs.bots.telegram.downloader;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Configuration
public class DownloaderConfig {
    @Bean("downloaderRestTemplate")
    public RestTemplate downloaderRestTemplate(){
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.of(2, ChronoUnit.MINUTES))
                .setConnectTimeout(Duration.of(2, ChronoUnit.MINUTES))
                .build();
    }
}
