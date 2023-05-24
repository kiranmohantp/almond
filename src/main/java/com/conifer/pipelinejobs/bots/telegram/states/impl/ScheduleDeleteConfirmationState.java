package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("scheduleDeleteConfirmation")
public class ScheduleDeleteConfirmationState implements com.conifer.pipelinejobs.bots.telegram.states.State {
    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Are you sure You want to delete ?")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(List.of(new EasyAccessOption("Delete", inBoundMessage.getMessage()
                                .replace(StateConstents.SCHEDULE_DELETE_CONFIRM_PREFIX, StateConstents.SCHEDULE_DELETE_CONFIRMED_PREFIX)))
                )
                .build();
        return outBoundMessage;
    }
}
