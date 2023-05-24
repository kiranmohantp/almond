package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.conifer.pipelinejobs.deviceoperations.SensorOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("sensorReadingState")
public class SensorReadingState implements State {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    @Qualifier("deviceNotFoundState")
    private State deviceNotFoundState;

    @Autowired
    @Qualifier("genericSensorOperation")
    private SensorOperations genericSensorOperation;


    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        LOGGER.debug("reading sensor data.");
        var splintedCommand = inBoundMessage.getMessage().split(StateConstents.COMMAND_DELIMITER);
        var deviceName = splintedCommand[2];
        var device = deviceDiscoveryService.getDeviceByName(deviceName);
        if (!device.isPresent()) {
            return deviceNotFoundState.execute(inBoundMessage);
        }
        try {
            String responseFromSensor = genericSensorOperation.readData(device.get());
            return createResponse("Response : " + responseFromSensor, inBoundMessage, createAvailableEasyAccessOptions());
        } catch (Exception e) {
            return createResponse("Unable To Connect", inBoundMessage, createAvailableEasyAccessOptionsOnError(device.get()));
        }
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> AvailableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message(message)
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(AvailableEasyAccessOptionsOnError)
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions() {
        return List.of(new EasyAccessOption("List Devices", StateConstents.LIST_PREFIX));
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptionsOnError(Device device) {
        return List.of(new EasyAccessOption("Retry", StateConstents.MORE_INFO_PREFIX + device.getName()));
    }
}
