package com.conifer.pipelinejobs.bots.telegram.downloader;

import com.conifer.pipelinejobs.bots.telegram.notification.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Downloader {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RestTemplate downloaderRestTemplate;
    @Value("${download.output.dir}")
    private String outputDirectory;
    @Value("${bot.token}")
    private String token;
    @Value("${telegram.download.getfilename.url}")
    private String getDownloadFileNameUrl;
    @Value("${telegram.download.url}")

    private String downloadFileUrl;
    @Autowired
    @Qualifier("telegramSpecificChatRoomNotificationService")
    private NotificationService notificationService;

    @Async
    public void downloadImageIfPresent(Update update) {
        if (update.getMessage() != null && update.getMessage().getPhoto() != null) {
            var largeFile = update.getMessage().getPhoto()
                    .stream().sorted((o1, o2) -> Integer.compare(o2.getFileSize(), o1.getFileSize())).findFirst();
            performDownload(largeFile.get().getFileId());
        }
    }


    @Async
    public void downloadVideoIfPresent(Update update) {
        if (update.getMessage() != null && update.getMessage().getVideo() != null) {
            performDownload(update.getMessage().getVideo().getFileId());
        }
    }

    @Async
    public void downloadDocumentIfPresent(Update update) {
        if (update.getMessage() != null && update.getMessage().getDocument() != null) {
            performDownload(update.getMessage().getDocument().getFileId());
        }
    }

    private void performDownload(String fileId) {
        try {
            String url = String.format(getDownloadFileNameUrl, token, fileId);
            String fileNameResponse = downloaderRestTemplate.getForEntity(url, String.class).getBody();
            String fileName = new ObjectMapper().readTree(fileNameResponse).get("result").get("file_path").asText();
            url = String.format(downloadFileUrl, token, fileName);
            byte[] imageBytes = downloaderRestTemplate.getForObject(url, byte[].class);
            Files.write(Paths.get(outputDirectory + fileName.replace("/", "/" + System.currentTimeMillis() + "_")), imageBytes);
            notificationService.sendNotification("Backup completed.");
        } catch (Exception e) {
            notificationService.sendNotification("Backup failed: " + e.getMessage());
            LOGGER.error("Error downloading file ", e);
        }
    }
}
