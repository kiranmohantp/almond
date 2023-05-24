package com.conifer.pipelinejobs.deviceoperations.controller;

import com.conifer.pipelinejobs.bots.scheduler.entity.DeviceOperation;
import com.conifer.pipelinejobs.bots.scheduler.exceptions.DeviceNotFoundException;
import com.conifer.pipelinejobs.devicediscovery.models.VirtualDevices;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DeviceOperationController {
    @Autowired
    @Qualifier("operationRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @GetMapping(value = "/execute/{virtualDeviceName}/{operation}")
    public ResponseEntity<String> executeOperation(@PathVariable("virtualDeviceName") String virtualDeviceName,
                                                   @PathVariable("operation") String operation) throws DeviceNotFoundException {
        var operationEnum = DeviceOperation.valueOf(operation.toUpperCase());
        VirtualDevices.valueOf(virtualDeviceName.toUpperCase()).performOperation(deviceDiscoveryService, restTemplate, operationEnum);
        return ResponseEntity.ok("Success");
    }
}
