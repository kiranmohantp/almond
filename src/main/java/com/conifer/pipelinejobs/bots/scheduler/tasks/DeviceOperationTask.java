package com.conifer.pipelinejobs.bots.scheduler.tasks;

import com.conifer.pipelinejobs.bots.telegram.notification.NotificationService;
import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.deviceoperations.SwitchOperations;
import lombok.AllArgsConstructor;

import java.util.TimerTask;
import java.util.function.BiConsumer;

@AllArgsConstructor
public class DeviceOperationTask extends TimerTask {
    private String taskName;
    private SwitchOperations operations;
    private String device;
    private BiConsumer<SwitchOperations, String> taskDefinition;
    private NotificationService notificationService;


    @Override
    public void run() {
        try {
            this.taskDefinition.accept(this.operations, this.device);
            notificationService.sendNotification(taskName + " Executed successfully.");
        } catch (Exception e) {
            notificationService.sendNotification(taskName + " Execution Failed !!");

        }
    }
}
