package com.conifer.pipelinejobs.deviceoperations.impl;

import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.deviceoperations.SensorOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component("genericSensorOperation")
public class GenericSensorOperation implements SensorOperations {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    @Qualifier("operationRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public String readData(Device device) {
        LOGGER.info("Calling sensor " + device.getName());
        return restTemplate.getForEntity("http://" + device.getIp() + "/reading", String.class).getBody();
    }
}
