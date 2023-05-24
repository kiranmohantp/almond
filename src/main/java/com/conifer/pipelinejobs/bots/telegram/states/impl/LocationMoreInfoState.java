package com.conifer.pipelinejobs.bots.telegram.states.impl;

import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import com.conifer.pipelinejobs.geospaceservices.entity.MarkedObject;
import com.conifer.pipelinejobs.geospaceservices.repositories.MarkedObjectRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("locationMoreInfo")
public class LocationMoreInfoState implements State {
    @Autowired
    private MarkedObjectRepository markedObjectRepository;

    @Override
    public OutBoundMessage execute(InBoundMessage inBoundMessage) {
        String name = inBoundMessage.getMessage().split("/")[2];
        Optional<MarkedObject> markedObject = markedObjectRepository.findByName(name);
        if (!markedObject.isPresent()) {
            return createResponse("No Location found.", inBoundMessage, List.of());
        }
        return createResponse(getMapUrl(markedObject.get()), inBoundMessage,
                List.of(new EasyAccessOption("Delete", StateConstents.DELETE_LOCATION_INFO_CONFIRM_PREFIX + markedObject.get().getName())));
    }

    @NotNull
    private String getMapUrl(MarkedObject markedObject) {
        return "<a href=\"https://maps.google.com/?q=[lat],[long]\">[name]</a>".replace("[lat]", markedObject.getLatitude().toString()).replace("[long]", markedObject.getLongitude().toString()).replace("[name]", markedObject.getName());
    }

    private OutBoundMessage createResponse(String message, InBoundMessage inBoundMessage, List<EasyAccessOption> availableEasyAccessOptionsOnError) {
        OutBoundMessage outBoundMessage = OutBoundMessage.builder().message(message).messageId(inBoundMessage.getMessageId()).easyAccessOptions(availableEasyAccessOptionsOnError).build();
        return outBoundMessage;
    }
}
