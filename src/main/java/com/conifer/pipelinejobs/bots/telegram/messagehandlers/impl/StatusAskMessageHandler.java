package com.conifer.pipelinejobs.bots.telegram.messagehandlers.impl;

import com.conifer.pipelinejobs.bots.telegram.messagehandlers.MessageHandler;
import com.conifer.pipelinejobs.bots.telegram.messagehandlers.StateFactory;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("statusAskMessageHandler")
public class StatusAskMessageHandler implements MessageHandler {
    @Autowired
    @Qualifier("genericStateFactory")
    private StateFactory stateFactory;

    @Override
    public OutBoundMessage handleMessage(InBoundMessage inBoundMessage) {
        return stateFactory.getState(inBoundMessage).execute(inBoundMessage);
    }

}
