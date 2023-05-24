package com.conifer.pipelinejobs.devicediscovery.controller;

import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class DeviceDiscoveryController {

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @PostMapping(value = "/register", consumes = {"application/json", "application/octet-stream"})
    public ResponseEntity<String> registerDevice(@RequestBody String device) throws JsonProcessingException {
        deviceDiscoveryService.addToDeviceList(new ObjectMapper().readValue(device, Device.class));
        return ResponseEntity.ok("Successfully added");
    }


    @GetMapping("/devices")
    public ResponseEntity<List<Device>> listDevices() {
        return ResponseEntity.ok(deviceDiscoveryService.getAllDevices());
    }
}
