package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("deviceNotFoundState")
public class DeviceNotFoundState implements State {

    @Autowired
    @Qualifier("unknownState")
    private State unknownState;

    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        var response = unknownState.execute(inBoundMessage);
        response.setMessage("Device not found! \n" + response.getMessage());
        return response;
    }
}
