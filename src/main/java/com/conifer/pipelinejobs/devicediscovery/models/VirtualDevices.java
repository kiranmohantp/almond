package com.conifer.pipelinejobs.devicediscovery.models;

import com.conifer.pipelinejobs.bots.scheduler.entity.DeviceOperation;
import com.conifer.pipelinejobs.bots.scheduler.exceptions.DeviceNotFoundException;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public enum VirtualDevices {
    ALL_FLOOD_LIGHTS("All Flood Lights", (deviceDiscoveryService, restTemplate, operation) -> {
        var lights = Arrays.asList("Top Light- Right", "Top Light- Front", "Top Light- Left");
        for(var light : lights){
            performDeviceOnOff(deviceDiscoveryService, restTemplate, operation, light);
        }
        return true;
    });

    private Device device;
    private TriFunction executeOperation;

    public Device getDevice() {
        return device;
    }

    public boolean performOperation(DeviceDiscoveryService deviceDiscoveryService, RestTemplate restTemplate, DeviceOperation operation) throws DeviceNotFoundException {
        return executeOperation.execute(deviceDiscoveryService, restTemplate, operation);
    }

    VirtualDevices(String name, TriFunction execute) {
        device = Device.virtualDevice(name, "192.168.1.200/execute/" + name.replace(" ", "_").toLowerCase());
        executeOperation = execute;
    }

    private static void performDeviceOnOff(DeviceDiscoveryService deviceDiscoveryService, RestTemplate restTemplate, DeviceOperation operation, String deviceName) throws DeviceNotFoundException {
        var device = deviceDiscoveryService.getDeviceByName(deviceName);
        if (device.isPresent()) {
            if (operation == DeviceOperation.ON) {
                restTemplate.getForEntity("http://" + device.get().getIp() + "/on", String.class);
            } else {
                restTemplate.getForEntity("http://" + device.get().getIp() + "/off", String.class);
            }
        }else{
            throw new DeviceNotFoundException();
        }
    }
}
