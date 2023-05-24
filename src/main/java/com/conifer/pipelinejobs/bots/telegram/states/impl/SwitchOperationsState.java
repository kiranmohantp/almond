package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.conifer.pipelinejobs.deviceoperations.SwitchOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("switchOperationsState")
public class SwitchOperationsState implements State {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    @Qualifier("genericSwitchOperations")
    private SwitchOperations switchOperationsState;

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    @Qualifier("unknownState")
    private State unknownState;

    @Autowired
    @Qualifier("deviceNotFoundState")
    private State deviceNotFoundState;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        var splintedCommand = inBoundMessage.getMessage().split(StateConstents.COMMAND_DELIMITER);
        var deviceName = splintedCommand[2];
        var device = deviceDiscoveryService.getDeviceByName(deviceName);
        OutBoundMessage outBoundMessage = null;
        if (!device.isPresent()) {
            return deviceNotFoundState.execute(inBoundMessage);
        }
        try {
            outBoundMessage = executeOperation(inBoundMessage, device.get());
        } catch (Exception e) {
            LOGGER.error("Switch execution error ", e);
            outBoundMessage = createResponse("Error!", inBoundMessage, List.of(new EasyAccessOption("Retry", inBoundMessage.getMessage())));
        }
        return outBoundMessage;
    }

    private OutBoundMessage executeOperation(InBoundMessage inBoundMessage, Device device) {
        if (inBoundMessage.getMessage().startsWith(StateConstents.DEVICE_ON_PREFIX)) {
            switchOperationsState.onSwitch(device);
            return createResponse(device.getName() + " switched on", inBoundMessage, createAvailableEasyAccessOptions(device));
        }
        if (inBoundMessage.getMessage().startsWith(StateConstents.DEVICE_OFF_PREFIX)) {
            switchOperationsState.offSwitch(device);
            return createResponse(device.getName() + " switched off", inBoundMessage, createAvailableEasyAccessOptions(device));
        }
        if (inBoundMessage.getMessage().startsWith(StateConstents.DEVICE_Status_PREFIX)) {
            var response = switchOperationsState.switchStatus(device);
            return createResponse(device.getName() + " is " + response, inBoundMessage, createAvailableEasyAccessOptions(device));

        }
        return unknownState.execute(inBoundMessage);
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> AvailableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message(message)
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(AvailableEasyAccessOptionsOnError)
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions(Device device) {
        return List.of(
                new EasyAccessOption("Switch on", StateConstents.DEVICE_ON_PREFIX + device.getName()),
                new EasyAccessOption("Switch off", StateConstents.DEVICE_OFF_PREFIX + device.getName()),
                new EasyAccessOption("Current Status", StateConstents.DEVICE_Status_PREFIX + device.getName()),
                new EasyAccessOption("Back", StateConstents.COMMAND_DELIMITER));
    }
}
