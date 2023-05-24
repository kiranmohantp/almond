package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.scheduler.repository.ScheduleRepository;
import com.conifer.pipelinejobs.bots.scheduler.runner.JobSchedulerService;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.geospaceservices.repositories.MarkedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("locationDeleteState")
public class DeleteLocationState implements State {
    @Autowired
    private MarkedObjectRepository markedObjectRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        String name = inBoundMessage.getMessage().split("/")[2];
        var location = markedObjectRepository.findByName(name);
        markedObjectRepository.delete(location.get());
        return OutBoundMessage.builder()
                .message(name + " Deleted ")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(List.of())
                .build();
    }
}
