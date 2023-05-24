package com.conifer.pipelinejobs.bots.telegram.security.impl;

import com.conifer.pipelinejobs.bots.telegram.exceptions.UnauthorizedAccessException;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.security.MessageSenderAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatIdBasedMessageSenderAuthenticator implements MessageSenderAuthenticator {
    @Value("${bot.chatId}")
    private Long chatId;

    @Override
    public void checkUserIsAuthorized(InBoundMessage inBoundMessage) throws UnauthorizedAccessException {
        if (!inBoundMessage.getMessageId().equals(chatId)) {
            throw new UnauthorizedAccessException("This message is not from the expected group.");
        }
    }
}
