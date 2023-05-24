package com.conifer.pipelinejobs.bots.telegram.converters.impl;

import com.conifer.pipelinejobs.bots.telegram.converters.AbBaConverter;
import com.conifer.pipelinejobs.bots.telegram.model.EasyAccessOption;
import com.conifer.pipelinejobs.bots.telegram.model.OutBoundMessage;
import com.conifer.pipelinejobs.bots.telegram.states.constents.StateConstents;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component("outBoundMessageSendMessageConverter")
public class OutBoundMessageToSendMessageConverter implements AbBaConverter<OutBoundMessage, SendMessage> {
    @Override
    public SendMessage convertFromAtoB(OutBoundMessage inputObject) {
        SendMessage message = new SendMessage();
        message.setText(inputObject.getMessage());
        message.enableHtml(true);
        message.setChatId(inputObject.getMessageId());
        message.setReplyMarkup(getAvailableOptionKeyBoard(inputObject.getEasyAccessOptions()));
        return message;
    }

    private InlineKeyboardMarkup getAvailableOptionKeyBoard(List<EasyAccessOption> keyboardOptions) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardLayout = new ArrayList<>();
        keyboardOptions.forEach(option -> {
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            var button = new InlineKeyboardButton();
            button.setText(option.getText());
            button.setCallbackData(option.getCallBackIdentifier());
            inlineKeyboardButtons.add(button);
            keyboardLayout.add(inlineKeyboardButtons);
        });
        var button = new InlineKeyboardButton();
        button.setText("Main Menu");
        button.setCallbackData(StateConstents.COMMAND_DELIMITER);
        keyboardLayout.add(Arrays.asList(button));
        inlineKeyboardMarkup.setKeyboard(keyboardLayout);
        return inlineKeyboardMarkup;
    }
}
