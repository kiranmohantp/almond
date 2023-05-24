package com.conifer.pipelinejobs.bots.telegram.messagehandlers;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.State;

public interface StateFactory {
    State getState(InBoundMessage inBoundMessage);
}
