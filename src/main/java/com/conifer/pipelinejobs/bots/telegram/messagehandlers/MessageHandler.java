package com.conifer.pipelinejobs.bots.telegram.messagehandlers;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;

public interface MessageHandler {
    OutBoundMessage handleMessage(InBoundMessage inBoundMessage);
}
