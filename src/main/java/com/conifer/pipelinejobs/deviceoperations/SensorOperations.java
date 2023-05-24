package com.conifer.pipelinejobs.deviceoperations;

import com.conifer.pipelinejobs.devicediscovery.models.Device;

public interface SensorOperations {
    String readData(Device device);
}
