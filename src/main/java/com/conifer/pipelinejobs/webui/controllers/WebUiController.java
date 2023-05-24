package com.conifer.pipelinejobs.webui.controllers;

import com.conifer.pipelinejobs.devicediscovery.models.DeviceTypes;
import com.conifer.pipelinejobs.devicediscovery.service.DeviceDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebUiController {
    @Autowired
    private DeviceDiscoveryService deviceDiscoveryService;

    @GetMapping(path = "/")
    public String uiScreen() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><body>");
        deviceDiscoveryService.getAllDevices().forEach(device -> {
            if (DeviceTypes.INFO == device.getType()) {
                stringBuilder.append("<div><b>").append(device.getName()).append(":</b>")
                        .append("<a href=\"http://").append(device.getIp()).append("/reading").append("\"><button style=\"background-color:gray;color:black;\">Read</button><a>")
                        .append("<div>");
            } else {
                stringBuilder.append("<div><b>").append(device.getName()).append(":</b>")
                        .append("<a href=\"http://").append(device.getIp()).append("/on").append("\"><button style=\"background-color:green;color:white;\">ON</button><a>")
                        .append("<a href=\"http://").append(device.getIp()).append("/off").append("\"><button style=\"background-color:red;color:white;\">OFF</button><a>")
                        .append("<div>");
            }
        });
        stringBuilder.append("<div></body></html>");
        return stringBuilder.toString();
    }
}
