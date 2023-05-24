package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("listDeviceState")
public class ListDeviceState implements State {
    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Available Devices Are")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(createAvailableEasyAccessOptions())
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions() {
        return deviceDiscoveryService.getAllDevices()
                .stream().map(device -> new EasyAccessOption(device.getName(), StateConstents.MORE_INFO_PREFIX + device.getName()))
                .collect(Collectors.toList());
    }
}
