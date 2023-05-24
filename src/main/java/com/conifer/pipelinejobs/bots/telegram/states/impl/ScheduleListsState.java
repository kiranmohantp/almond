package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.scheduler.repository.ScheduleRepository;
import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("scheduleListsState")
public class ScheduleListsState implements State {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        List<EasyAccessOption> easyAccessOptions = new ArrayList<>();
        scheduleRepository.findAll().forEach(schedule -> {
            easyAccessOptions.add(new EasyAccessOption(schedule.getJobName() + " at " + schedule.getStartTime(), StateConstents.SCHEDULE_DELETE_CONFIRM_PREFIX + schedule.getJobName()));
        });
        return createResponse("Available schedules are", inBoundMessage, easyAccessOptions);
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> availableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message(message)
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(availableEasyAccessOptionsOnError)
                .build();
        return outBoundMessage;
    }
}
