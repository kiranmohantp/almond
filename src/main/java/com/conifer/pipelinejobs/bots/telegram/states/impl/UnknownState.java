package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component("unknownState")
public class UnknownState implements State {
    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Available Options are.")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(createAvailableEasyAccessOptions())
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions() {
        return List.of(
                new EasyAccessOption("List Devices", StateConstents.LIST_PREFIX),
                new EasyAccessOption("Scheduled Tasks", StateConstents.SCHEDULE_LIST_PREFIX),
                new EasyAccessOption("Saved Location", StateConstents.LOCATION_LIST_PREFIX));
    }
}
