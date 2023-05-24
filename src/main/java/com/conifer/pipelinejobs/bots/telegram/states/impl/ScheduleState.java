package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.scheduler.exceptions.DeviceNotFoundException;
import com.conifer.pipelinejobs.bots.scheduler.repository.ScheduleRepository;
import com.conifer.pipelinejobs.bots.scheduler.runner.JobSchedulerService;
import com.conifer.pipelinejobs.bots.telegram.exceptions.ParsingException;
import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.parser.TaskScheduleRequestParser;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component("scheduleState")
public class ScheduleState implements State {
    @Autowired
    private TaskScheduleRequestParser taskScheduleRequestParser;
    @Autowired
    private JobSchedulerService jobSchedulerService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage;
        try {
            var schedule = taskScheduleRequestParser.createScheduleFromMessage(inBoundMessage.getMessage());
            jobSchedulerService.scheduleJob(schedule);
            scheduleRepository.save(schedule);
            outBoundMessage = createResponse("Created Successfully", inBoundMessage, List.of());
        } catch (ParsingException e) {
            outBoundMessage = createResponse(
                    " Message format is \n" +
                    " /@Name \n" +
                    " time    in minutes or hour:minute AM/PM\n" +
                    " description \n" +
                    " device name \n" +
                    " Operation ON/OFF", inBoundMessage, List.of());
        } catch (DeviceNotFoundException e) {
            outBoundMessage = createResponse("Device not found.", inBoundMessage, List.of());
        } catch (Exception e) {
            outBoundMessage = createResponse("Something unexpected happened.", inBoundMessage, List.of());
        }
        return outBoundMessage;
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> AvailableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message(message)
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(AvailableEasyAccessOptionsOnError)
                .build();
        return outBoundMessage;
    }
}
