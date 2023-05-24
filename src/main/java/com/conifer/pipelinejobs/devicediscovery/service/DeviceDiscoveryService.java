package com.conifer.pipelinejobs.devicediscovery.service;

import com.conifer.pipelinejobs.devicediscovery.models.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceDiscoveryService {
    void addToDeviceList(Device device);

    List<Device> getAllDevices();

    Optional<Device> getDeviceByName(String deviceName);
}
