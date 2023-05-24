package com.conifer.pipelinejobs.deviceoperations;

import com.conifer.pipelinejobs.devicediscovery.models.Device;

public interface SwitchOperations {
    void onSwitch(Device device);

    void offSwitch(Device device);

    String switchStatus(Device device);
}
