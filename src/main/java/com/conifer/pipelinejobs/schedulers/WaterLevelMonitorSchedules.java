package com.conifer.pipelinejobs.schedulers;

import com.conifer.pipelinejobs.bots.telegram.notification.NotificationService;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class WaterLevelMonitorSchedules {
    @Autowired
    @Qualifier("telegramSpecificChatRoomNotificationService")
    private NotificationService notificationService;

    @Autowired
    @Qualifier("deviceMonitorRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @Autowired
    private ObjectMapper objectMapper;

    private boolean motorStarted;
    private boolean sendFirstTimeManualSwitchOnSystemSwitchOffNotification = true;

    private LocalDateTime motorStartedAt;
    private Integer readingWhenMotorStarted;
    private int waterLevel = 0;

    private static String WATER_LEVEL_SENSOR = "WL T H Readings";
    private static String MAIN_MOTOR_SWITCH = "MainMotorSwitch";
    private static int WATER_INCREASING_CHECK_TIME_IN_MINUTES = 3;
    private static int MAX_MOTOR_ALLOWED_TIME_IN_MINUTES = 14;


    Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(fixedRate = 5000)
    public void checkWaterLevelAndStartOrStopMotorIfNeeded() throws JsonProcessingException {
        LOGGER.info("Checking water level");
        var sensor = deviceDiscoveryService.getDeviceByName(WATER_LEVEL_SENSOR);
        if (sensor.isPresent()) {
            String url = "http://" + sensor.get().getIp() + "/json/summery";
            var response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
            var responseAsJsonTree = objectMapper.readTree(response.getBody());
            var waterPercentage = responseAsJsonTree.get("waterPercentage").asInt();
            if(waterPercentage == 0) return;
            if(waterLevel != waterPercentage){
                if(waterPercentage == 100){
                    sendFirstTimeManualSwitchOnSystemSwitchOffNotification = true;
                }
                waterLevel = waterPercentage;
                if(waterLevel == 100 || waterLevel == 10){
                    notificationService.sendNotification("Water level is " + waterLevel +"%");
                }
            }
            checkWaterLevelIncreasingWhenMotorRunningAndNotifyIfNot(waterPercentage);
            checkAndStartMotor(waterPercentage);
            failSafeCheckAndStopMotor(waterPercentage);
        } else {
            LOGGER.info("No sensor details found.");
        }
    }

    private void checkWaterLevelIncreasingWhenMotorRunningAndNotifyIfNot(int waterPercentage) {
        if (motorStarted
                && ChronoUnit.MINUTES.between(LocalDateTime.now(), motorStartedAt) > WATER_INCREASING_CHECK_TIME_IN_MINUTES
                && readingWhenMotorStarted == waterPercentage) { // if motor started and water level not increasing within 3 minutes
            notificationService.sendNotification("Error : Water level not increasing !");
            motorStarted = false; // do not repeat this message to the user.
        }
    }

    private void checkAndStartMotor(int waterPercentage) {
        if (waterPercentage == 10 && !motorStarted) {
            var motor = deviceDiscoveryService.getDeviceByName(MAIN_MOTOR_SWITCH);
            if (motor.isPresent()) {
                String url = "http://" + motor.get().getIp() + "/on";
                try {
                    restTemplate.getForEntity(url, String.class);
                    motorStarted = true;
                    motorStartedAt = LocalDateTime.now();
                    readingWhenMotorStarted = waterPercentage;
                    notificationService.sendNotification("Low water,Motor Started!");
                } catch (Exception e) {
                    LOGGER.error("Motor starting failed.", e);
                    notificationService.sendNotification("Motor starting failed.");
                }
            }
        }
    }

    private void failSafeCheckAndStopMotor(int waterPercentage) {
        if (waterPercentage == 100 || (motorStarted &&  ChronoUnit.MINUTES.between(LocalDateTime.now(), motorStartedAt) > MAX_MOTOR_ALLOWED_TIME_IN_MINUTES)) {
            var motor = deviceDiscoveryService.getDeviceByName(MAIN_MOTOR_SWITCH);
            if (motor.isPresent()) {
                String url = "http://" + motor.get().getIp() + "/off";
                try {
                    restTemplate.getForEntity(url, String.class);
                    if (motorStarted || sendFirstTimeManualSwitchOnSystemSwitchOffNotification) {
                        notificationService.sendNotification("Motor Stopped.");
                        motorStarted = false;
                        sendFirstTimeManualSwitchOnSystemSwitchOffNotification = false;
                    }
                } catch (Exception e) {
                    LOGGER.error("Error! Unable to stop motor.", e);
                    if (motorStarted) {
                        notificationService.sendNotification("Error! Unable to stop motor.");
                    }
                }
            }
        }
    }
}

