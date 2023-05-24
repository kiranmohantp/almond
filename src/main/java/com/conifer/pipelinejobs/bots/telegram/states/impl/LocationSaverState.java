package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.geospaceservices.entity.MarkedObject;
import com.conifer.pipelinejobs.geospaceservices.repositories.MarkedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("locationSaver")
public class LocationSaverState implements State {
    @Autowired
    private MarkedObjectRepository markedObjectRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        try {
            markedObjectRepository.save(MarkedObject.builder()
                    .longitude(inBoundMessage.getLongitude())
                    .latitude(inBoundMessage.getLatitude())
                    .name(inBoundMessage.getMessage().toLowerCase().trim())
                    .build());
        }catch (DataIntegrityViolationException e){
            return createResponse("Duplicate Name", inBoundMessage, List.of());
        }
        return createResponse("Location saved", inBoundMessage, List.of());
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> AvailableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder()
                .message(message)
                .messageId(inBoundMessage.getMessageId())
                .easyAccessOptions(AvailableEasyAccessOptionsOnError)
                .build();
        return outBoundMessage;
    }
}
