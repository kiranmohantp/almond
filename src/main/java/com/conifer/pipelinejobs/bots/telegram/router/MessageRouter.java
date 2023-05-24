package com.conifer.pipelinejobs.bots.telegram.router;

import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;

public interface MessageRouter {
    OutBoundMessage routeToAppropriateHandler(InBoundMessage inboundMessage);
}
