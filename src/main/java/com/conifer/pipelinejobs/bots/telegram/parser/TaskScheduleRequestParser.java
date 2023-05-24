package com.conifer.pipelinejobs.bots.telegram.parser;

import com.conifer.pipelinejobs.bots.scheduler.entity.DeviceOperation;
import com.conifer.pipelinejobs.bots.scheduler.entity.Schedule;
import com.conifer.pipelinejobs.bots.telegram.exceptions.ParsingException;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Message format
 * ^Name \n
 * time \n   in minutes or hour:minute:AM/PM
 * description \n
 * device name \n
 * Operation ON/OFF
 */
@Component
public class TaskScheduleRequestParser {
    public Schedule createScheduleFromMessage(String message) throws ParsingException {
        try {
            var splitedMessage = message.replace(StateConstents.SCHEDULE_TASK_PREFIX, "").split("\\n");
            Schedule schedule = new Schedule();
            schedule.setJobName(splitedMessage[0]);
            schedule.setStartTime(parseTime(splitedMessage[1]));
            schedule.setRunAlwaysInThisMilliSeconds(parseMinutesAndReturnInMilliseconds(splitedMessage[1]));
            schedule.setDescription(splitedMessage[2]);
            schedule.setDeviceName(splitedMessage[3]);
            schedule.setOperation(Enum.valueOf(DeviceOperation.class, splitedMessage[4].toUpperCase()));
            return schedule;
        } catch (Exception e) {
            throw new ParsingException("Parsing failed!", e);
        }
    }

    private LocalTime parseTime(String timeRepresentation) {
        if (timeRepresentation.contains(":")) {
            return LocalTime.parse(timeRepresentation, DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
        }
        return null;
    }

    private Long parseMinutesAndReturnInMilliseconds(String timeRepresentation) {
        if (!timeRepresentation.contains(":")) {
            return Long.parseLong(timeRepresentation) * 60000;
        }
        return null;
    }
}
