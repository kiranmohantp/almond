package com.conifer.pipelinejobs.bots.telegram.router.impl;

import com.conifer.pipelinejobs.bots.telegram.messagehandlers.MessageHandler;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.router.MessageRouter;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("genericMessageRouter")
public class GenericMessageRouter implements MessageRouter {
    @Autowired
    @Qualifier("statusAskMessageHandler")
    private MessageHandler messageHandler;

    @Override
    public OutBoundMessage routeToAppropriateHandler(InBoundMessage inboundMessage) {
        //only status ask is implemented for now. natural language processing will be integrated soon.
        return messageHandler.handleMessage(inboundMessage);
    }


}
