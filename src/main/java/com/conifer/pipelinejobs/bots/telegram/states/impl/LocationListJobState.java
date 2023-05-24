package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.geospaceservices.repositories.MarkedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("locationListJobState")
public class LocationListJobState implements State {
    @Autowired
    private MarkedObjectRepository markedObjectRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message("Saved Locations Are")
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(createAvailableEasyAccessOptions())
                .build();
        return outBoundMessage;
    }

    private List<EasyAccessOption> createAvailableEasyAccessOptions(){
        List<EasyAccessOption> easyAccessOptions = new ArrayList<>();
        markedObjectRepository.findAll().forEach(markedObject -> {
            easyAccessOptions.add(new EasyAccessOption(markedObject.getName(), StateConstents.MORE_LOCATION_INFO_PREFIX + markedObject.getName()));
        });
        return easyAccessOptions;
    }
}
