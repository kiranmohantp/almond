package com.conifer.pipelinejobs.devicediscovery.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Device {
    @NotNull
    private String name;
    @NotNull
    private String ip;
    @NotNull
    private DeviceTypes type;
    private Long lastHearBeat;
    private Boolean markedDead;

    public static Device virtualDevice(String name, String ip) {
        Device device = new Device();
        device.setName(name);
        device.setIp(ip);
        device.setLastHearBeat(Long.MAX_VALUE);
        device.setType(DeviceTypes.SWITCH);
        device.setMarkedDead(false);
        return device;
    }
}


