package com.conifer.pipelinejobs.bots.scheduler.runner;

import com.conifer.pipelinejobs.bots.scheduler.entity.DeviceOperation;
import com.conifer.pipelinejobs.bots.scheduler.entity.Schedule;
import com.conifer.pipelinejobs.bots.scheduler.exceptions.DeviceNotFoundException;
import com.conifer.pipelinejobs.bots.scheduler.repository.ScheduleRepository;
import com.conifer.pipelinejobs.bots.scheduler.tasks.DeviceOperationTask;
import com.conifer.pipelinejobs.bots.telegram.notification.NotificationService;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.conifer.pipelinejobs.deviceoperations.SwitchOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;

@Service
public class JobSchedulerService {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    @Qualifier("genericSwitchOperations")
    private SwitchOperations operations;

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    @Qualifier("telegramSpecificChatRoomNotificationService")
    private NotificationService notificationService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Timer timer = new Timer();

    @EventListener(ApplicationStartedEvent.class)
    @Transactional
    public void restoreScheduler() {
        timer = new Timer();
        scheduleRepository.findAll().forEach(schedule -> {
            try {
                scheduleJob(schedule);
                LOGGER.info(schedule.getJobName() + "- Scheduled");
            } catch (DeviceNotFoundException e) {
                LOGGER.error(schedule.getJobName() + "- Scheduling failed");
                notificationService.sendNotification(schedule.getJobName() + "- Scheduling failed");
            }
        });
    }

    public void scheduleJob(Schedule schedule) throws DeviceNotFoundException {
        if (schedule.getStartTime() != null) {
            LocalDateTime dateTime1 = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            LocalDateTime dateTime2 = LocalDateTime.of(LocalDate.now(), schedule.getStartTime());
            Duration duration = Duration.between(dateTime1, dateTime2);
            long oneDayInMilliSeconds = 24 * 60 * 60 * 1000;
            long milliseconds = duration.toMillis() < 0 ? (-duration.toMillis()) + oneDayInMilliSeconds : duration.toMillis();
            timer.schedule(createTask(schedule), milliseconds, oneDayInMilliSeconds);
            return;
        }
        if (schedule.getRunAlwaysInThisMilliSeconds() != null) {
            timer.schedule(createTask(schedule), schedule.getRunAlwaysInThisMilliSeconds(), schedule.getRunAlwaysInThisMilliSeconds());
            return;
        }
    }

    private DeviceOperationTask createTask(Schedule schedule) {
        if (schedule.getOperation() == DeviceOperation.ON) {
            return deviceOnTask(schedule.getDeviceName(), schedule.getJobName());
        } else {
            return deviceOffTask(schedule.getDeviceName(), schedule.getJobName());
        }
    }

    private DeviceOperationTask deviceOnTask(String device, String taskName) {
        DeviceOperationTask deviceOperationTask = new DeviceOperationTask(taskName, operations, device, (o, deviceName) -> {
            o.onSwitch(deviceDiscoveryService.getDeviceByName(deviceName).get());
        }, notificationService);
        return deviceOperationTask;
    }

    private DeviceOperationTask deviceOffTask(String device, String taskName) {
        DeviceOperationTask deviceOperationTask = new DeviceOperationTask(taskName, operations, device, (o, deviceName) -> {
            o.offSwitch(deviceDiscoveryService.getDeviceByName(deviceName).get());
        }, notificationService);
        return deviceOperationTask;
    }
}
