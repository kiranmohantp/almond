package com.conifer.pipelinejobs.devicediscovery.models;

import com.conifer.pipelinejobs.bots.scheduler.entity.DeviceOperation;
import com.conifer.pipelinejobs.bots.scheduler.exceptions.DeviceNotFoundException;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.web.client.RestTemplate;

@FunctionalInterface
public interface TriFunction {
    Boolean execute(DeviceDiscoveryService devices, RestTemplate restTemplate, DeviceOperation operation) throws DeviceNotFoundException;
}
