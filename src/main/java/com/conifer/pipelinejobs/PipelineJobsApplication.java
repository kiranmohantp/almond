package com.conifer.pipelinejobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PipelineJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipelineJobsApplication.class, args);
	}

}
