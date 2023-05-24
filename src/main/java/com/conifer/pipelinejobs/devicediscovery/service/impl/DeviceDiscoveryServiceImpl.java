package com.conifer.pipelinejobs.devicediscovery.service.impl;

import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.models.VirtualDevices;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Validated
public class DeviceDiscoveryServiceImpl implements DeviceDiscoveryService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    private Map<String, Device> deviceList = defaultDeviceGroups();

    private Long DEVICE_MARKED_AS_DEAD_TIMEOUT = 1000 * 60 * 4l;

    @Override
    public void addToDeviceList(@Valid Device device) {
        LOGGER.info(device.getName() + " " + device.getIp() + " -- registered" );
        device.setLastHearBeat(System.currentTimeMillis());
        deviceList.put(device.getName(), device);
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceList.entrySet()
                .stream()
                .map(stringDeviceEntry -> stringDeviceEntry.getValue())
                .filter(device -> System.currentTimeMillis() - device.getLastHearBeat() < DEVICE_MARKED_AS_DEAD_TIMEOUT)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Device> getDeviceByName(String deviceName) {
        return deviceList.entrySet()
                .stream()
                .map(stringDeviceEntry -> stringDeviceEntry.getValue())
                .filter(device -> device.getName().equalsIgnoreCase(deviceName))
                .findFirst();
    }

    private Map<String, Device> defaultDeviceGroups() {
        Map<String, Device> deviceList = new ConcurrentHashMap();
        deviceList.put(VirtualDevices.ALL_FLOOD_LIGHTS.getDevice().getName(),
                VirtualDevices.ALL_FLOOD_LIGHTS.getDevice());
        return deviceList;
    }
}
