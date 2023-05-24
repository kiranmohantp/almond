package com.conifer.pipelinejobs.bots.telegram.security;

import com.conifer.pipelinejobs.bots.telegram.exceptions.UnauthorizedAccessException;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;

public interface MessageSenderAuthenticator {
    void checkUserIsAuthorized(InBoundMessage inBoundMessage) throws UnauthorizedAccessException;
}
