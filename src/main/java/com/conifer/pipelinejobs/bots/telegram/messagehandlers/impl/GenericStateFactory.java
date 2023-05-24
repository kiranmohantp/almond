package com.conifer.pipelinejobs.bots.telegram.messagehandlers.impl;

import com.conifer.pipelinejobs.bots.telegram.messagehandlers.StateFactory;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents.*;

@Component("genericStateFactory")
public class GenericStateFactory implements StateFactory {
    @Autowired
    @Qualifier("listDeviceState")
    private State listDeviceState;

    @Autowired
    @Qualifier("unknownState")
    private State unknownState;

    @Autowired
    @Qualifier("moreInfoState")
    private State moreInfoState;

    @Autowired
    @Qualifier("switchOperationsState")
    private State switchOperationsState;

    @Autowired
    @Qualifier("scheduleState")
    private State scheduleState;

    @Autowired
    @Qualifier("scheduleListsState")
    private State scheduleListsState;

    @Autowired
    @Qualifier("scheduleDeleteConfirmation")
    private State scheduleDeleteConfirmation;

    @Autowired
    @Qualifier("deleteJobState")
    private State deleteJobState;

    @Autowired
    @Qualifier("locationListJobState")
    private State locationListJobState;

    @Autowired
    @Qualifier("locationSaver")
    private State locationSaver;

    @Autowired
    @Qualifier("locationMoreInfo")
    private State locationMoreInfo;

    @Autowired
    @Qualifier("locationDeleteConfirmation")
    private State deleteLocationConfirmation;

    @Autowired
    @Qualifier("locationDeleteState")
    private State deleteLocation;


    @Override
    public State getState(InBoundMessage inBoundMessage) {
        String message = inBoundMessage.getMessage();
        if (inBoundMessage.getLatitude() != null && inBoundMessage.getLongitude() != null) {
            return locationSaver;
        }
        if (message.startsWith(LOCATION_LIST_PREFIX)) {
            return locationListJobState;
        }
        if (message.startsWith(LIST_PREFIX)) {
            return listDeviceState;
        }
        if (message.startsWith(MORE_INFO_PREFIX)) {
            return moreInfoState;
        }
        if (message.startsWith(DEVICE_OPERATION_PREFIX)) {
            return switchOperationsState;
        }
        if (message.startsWith(SCHEDULE_TASK_PREFIX)) {
            return scheduleState;
        }
        if (message.startsWith(StateConstents.SCHEDULE_LIST_PREFIX)) {
            return scheduleListsState;
        }
        if (message.startsWith(SCHEDULE_DELETE_CONFIRM_PREFIX)) {
            return scheduleDeleteConfirmation;
        }
        if (message.startsWith(SCHEDULE_DELETE_CONFIRMED_PREFIX)) {
            return deleteJobState;
        }
        if (message.startsWith(MORE_LOCATION_INFO_PREFIX)) {
            return locationMoreInfo;
        }
        if (message.startsWith(DELETE_LOCATION_INFO_CONFIRM_PREFIX)) {
            return deleteLocationConfirmation;
        }
        if (message.startsWith(DELETE_LOCATION_INFO_PREFIX)) {
            return deleteLocation;
        }
        return unknownState;
    }
}
