package com.conifer.pipelinejobs.bots.telegram.states;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;

public interface State {
    OutBoundMessage execute(InBoundMessage inBoundMessage);
}
