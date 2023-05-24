package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.scheduler.repository.ScheduleRepository;
import com.conifer.pipelinejobs.bots.scheduler.runner.JobSchedulerService;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component("deleteJobState")
public class DeleteJobState implements State {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        String jobName = inBoundMessage.getMessage().split("/")[2];
        var schedule = scheduleRepository.findByJobName(jobName);
        scheduleRepository.delete(schedule);
        jobSchedulerService.restoreScheduler();
        return OutBoundMessage.builder()
                .message(jobName + " Deleted ")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(List.of())
                .build();
    }
}
