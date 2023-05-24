package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("locationDeleteConfirmation")
public class LocationDeleteConfirmationState implements com.conifer.pipelinejobs.bots.telegram.states.State {
    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Are you sure You want to delete ?")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(List.of(new EasyAccessOption("Delete", inBoundMessage.getMessage()
                                .replace(StateConstents.DELETE_LOCATION_INFO_CONFIRM_PREFIX, StateConstents.DELETE_LOCATION_INFO_PREFIX)))
                )
                .build();
        return outBoundMessage;
    }
}
