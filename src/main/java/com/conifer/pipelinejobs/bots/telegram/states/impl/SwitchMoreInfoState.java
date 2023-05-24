package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.conifer.pipelinejobs.deviceoperations.SwitchOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("switchMoreInfoState")
public class SwitchMoreInfoState implements State {
    @Autowired
    @Qualifier("genericSwitchOperations")
    private SwitchOperations switchOperations;

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    @Qualifier("deviceNotFoundState")
    private State deviceNotFoundState;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        var splintedCommand = inBoundMessage.getMessage().split(StateConstents.COMMAND_DELIMITER);
        var deviceName = splintedCommand[2];
        var device = deviceDiscoveryService.getDeviceByName(deviceName);
        if (!device.isPresent()) {
            return deviceNotFoundState.execute(inBoundMessage);
        }
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Supported Operations")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(createAvailableEasyAccessOptions(device.get()))
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions(Device device) {
        return List.of(
                new EasyAccessOption("Switch on", StateConstents.DEVICE_ON_PREFIX + device.getName()),
                new EasyAccessOption("Switch off", StateConstents.DEVICE_OFF_PREFIX + device.getName()),
                new EasyAccessOption("Current Status", StateConstents.DEVICE_Status_PREFIX + device.getName())
        );
    }
}
