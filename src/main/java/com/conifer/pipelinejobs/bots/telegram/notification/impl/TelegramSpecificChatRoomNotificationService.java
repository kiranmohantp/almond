package com.conifer.pipelinejobs.bots.telegram.notification.impl;

import com.conifer.pipelinejobs.bots.telegram.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("telegramSpecificChatRoomNotificationService")
public class TelegramSpecificChatRoomNotificationService implements NotificationService {
    @Value("${bot.token}")
    private String token;

    @Value("${bot.name}")
    private String name;

    @Value("${bot.chatId}")
    private Long chatId;

    @Value("${telegram.url}")
    private String urlString;

    @Autowired
    @Qualifier("notificationRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public void sendNotification(String message) {
        String url = String.format(urlString, token, chatId, message);
        restTemplate.getForEntity(url, String.class);
    }
}
