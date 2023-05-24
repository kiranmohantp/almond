package com.conifer.pipelinejobs.deviceoperations.impl;

import com.conifer.pipelinejobs.devicediscovery.models.Device;
import com.conifer.pipelinejobs.deviceoperations.SwitchOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("genericSwitchOperations")
public class GenericSwitchOperations implements SwitchOperations {
    @Autowired
    @Qualifier("operationRestTemplate")
    private RestTemplate restTemplate;

    @Override

    public void onSwitch(Device device) {
        restTemplate.getForEntity("http://" + device.getIp() + "/on", String.class);
    }

    @Override
    public void offSwitch(Device device) {
        restTemplate.getForEntity("http://" + device.getIp() + "/off", String.class);
    }

    @Override
    public String switchStatus(Device device) {
        String response = restTemplate.getForEntity("http://" + device.getIp() + "/status", String.class).getBody();
        return response.contains("true") ? "On" : "Off";
    }
}
