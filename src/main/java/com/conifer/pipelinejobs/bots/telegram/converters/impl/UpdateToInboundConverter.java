package com.conifer.pipelinejobs.bots.telegram.converters.impl;

import com.conifer.pipelinejobs.bots.telegram.converters.AbBaConverter;
import com.conifer.pipelinejobs.bots.telegram.model.InBoundMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("updateToInboundConverter")
public class UpdateToInboundConverter implements AbBaConverter<Update, InBoundMessage> {
    @Override
    public InBoundMessage convertFromAtoB(Update inputObject) {
        return InBoundMessage
                .builder()
                .message(getMessage(inputObject))
                .messageId(getMessageId(inputObject))
                .latitude(getLatitude(inputObject))
                .longitude(getLongitude(inputObject))
                .build();
    }

    private Long getMessageId(Update inputObject) {
        return inputObject.getMessage() == null ?
                inputObject.getCallbackQuery().getMessage().getChatId() : inputObject.getMessage().getChat().getId();
    }

    private String getMessage(Update inputObject) {
        if(isReplayMessage(inputObject)){
            inputObject.getMessage().getReplyToMessage().getText();
        }
        return inputObject.getMessage() == null ?
                inputObject.getCallbackQuery().getData() : inputObject.getMessage().getText();
    }

    private boolean isReplayMessage(Update inputObject) {
        return inputObject.getMessage() != null &&
                inputObject.getMessage().getReplyToMessage() != null;
    }

    private Double getLatitude(Update inputObject) {
        return isReplayMessage(inputObject) &&
                inputObject.getMessage().getReplyToMessage().getLocation() != null &&
                inputObject.getMessage().getReplyToMessage().getLocation() !=null ?  inputObject.getMessage().getReplyToMessage().getLocation().getLatitude() : null;
    }
    private Double getLongitude(Update inputObject) {
        return isReplayMessage(inputObject)  &&
                inputObject.getMessage().getReplyToMessage().getLocation() != null &&
                inputObject.getMessage().getReplyToMessage().getLocation() !=null ?  inputObject.getMessage().getReplyToMessage().getLocation().getLongitude() : null;
    }
}
