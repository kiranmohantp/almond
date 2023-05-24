package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.models.DeviceTypes;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("moreInfoState")
public class MoreInfoState implements State {
    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    @Qualifier("sensorReadingState")
    private State sensorReadingState;

    @Autowired
    @Qualifier("switchMoreInfoState")
    private State switchMoreInfoState;

    @Autowired
    @Qualifier("deviceNotFoundState")
    private State deviceNotFoundState;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        var device = getDevice(inBoundMessage);
        if (device == null) {
            return deviceNotFoundState.execute(inBoundMessage);
        }
        if (isRequestDeviceIsSensorType(device)) {
            return sensorReadingState.execute(inBoundMessage);
        } else {
            return switchMoreInfoState.execute(inBoundMessage);
        }
    }

    private boolean isRequestDeviceIsSensorType(Device device) {
        return device.getType().equals(DeviceTypes.INFO);
    }

    private Device getDevice(InBoundMessage inBoundMessage) {
        var splintedCommand = inBoundMessage.getMessage().split(StateConstents.COMMAND_DELIMITER);
        String deviceName = splintedCommand[2];
        var device = deviceDiscoveryService.getDeviceByName(deviceName);
        return device.get();
    }
}
