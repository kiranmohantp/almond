package com.conifer.pipelinejobs.bots.telegram.poller;

import com.conifer.pipelinejobs.bots.telegram.converters.AbBaConverter;
import com.conifer.pipelinejobs.bots.telegram.downloader.Downloader;
import com.conifer.pipelinejobs.bots.telegram.exceptions.UnauthorizedAccessException;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.router.MessageRouter;
import com.conifer.pipelinejobs.bots.telegram.security.MessageSenderAuthenticator;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramMessagePoller extends TelegramLongPollingBot {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bot.token}")
    private String token;

    @Value("${bot.name}")
    private String name;

    @Autowired
    private MessageSenderAuthenticator messageSenderAuthenticator;

    @Autowired
    @Qualifier("updateToInboundConverter")
    private AbBaConverter<Update, InBoundMessage> updateToInboundConverter;

    @Autowired
    @Qualifier("outBoundMessageSendMessageConverter")
    private AbBaConverter<OutBoundMessage, SendMessage> outBoundMessageSendMessageConverter;

    @Autowired
    @Qualifier("genericMessageRouter")
    private MessageRouter messageRouter;

    @Autowired
    private Downloader downloader;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            performDownloadIfPresent(update);
            var inboundMessage = updateToInboundConverter.convertFromAtoB(update);
            LOGGER.info("Incoming message " + inboundMessage.getMessage());
            messageSenderAuthenticator.checkUserIsAuthorized(inboundMessage);
            if (!checkMessageIsForBot(inboundMessage)) return; //do not proceed if the message is not for the bot
            var outboundMessage = messageRouter.routeToAppropriateHandler(inboundMessage);
            var reply = outBoundMessageSendMessageConverter.convertFromAtoB(outboundMessage);
            execute(reply);
        } catch (TelegramApiException e) {
            LOGGER.error("Unable to send telegram message ", e);
        } catch (UnauthorizedAccessException e) {
            LOGGER.error("Unauthorized Access Detected ");
        }
    }

    private void performDownloadIfPresent(Update update) {
        downloader.downloadImageIfPresent(update);
        downloader.downloadVideoIfPresent(update);
        downloader.downloadDocumentIfPresent(update);
    }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    private boolean checkMessageIsForBot(InBoundMessage inBoundMessage) {
        return inBoundMessage.getMessage() != null &&
                (inBoundMessage.getMessage().startsWith(StateConstents.COMMAND_DELIMITER) ||
                        (inBoundMessage.getLongitude() != null && inBoundMessage.getLatitude() != null));
    }
}