package com.rayshan.bot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.File;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class BotService extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TutorialBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(">>> " + update.getMessage());
        //System.out.println(update.getMessage().getText());

        try {
            Message inMessage = update.getMessage();
            if (inMessage.hasText()) {
                String incomingMsg = inMessage.getText();
                log.info("Received message {}", incomingMsg);

                if (incomingMsg.equalsIgnoreCase("/image")) {
                    // Send sample png file.
                    InputFile inputFile = new InputFile(new File("Sample.png"));
                    SendPhoto message = new SendPhoto();
                    message.setPhoto(inputFile);
                    message.setChatId(inMessage.getChatId().toString());
                    execute(message); // Call method to send the message
                } else {
                    SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                    message.setChatId(inMessage.getChatId().toString());
                    message.setText(inMessage.getText());
                    execute(message); // Call method to send the message
                }
            } else if (inMessage.hasDocument()) {
                log.info("Document present....");
                String fileId = inMessage.getDocument().getFileId();
                String mimeType = inMessage.getDocument().getMimeType();
                log.info("fileId {}, mimeType {}", fileId, mimeType);
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                String filePath = execute(getFile).getFilePath();
                String fileName = "downloads/file" + new Random().nextInt(100);
                if (mimeType.endsWith("pdf")) {
                    fileName += ".pdf";
                } else {
                    fileName += ".txt";
                }
                downloadFile(filePath, new File(fileName));

                SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message.setChatId(inMessage.getChatId().toString());
                message.setText("FileId " + fileId + ", mimeType " + mimeType);
                execute(message);

            } else if (inMessage.hasPhoto()) {
                log.info("Image present....");
                List<PhotoSize> photos = inMessage.getPhoto();
                if(photos.size() > 0) {
                    PhotoSize photo = photos.get(3);
                    String fileId = photo.getFileId();
                    GetFile getFileRequest = new GetFile();
                    getFileRequest.setFileId(fileId);
                    String filePath = execute(getFileRequest).getFilePath();
                    File newFile = downloadFile(filePath, new File("downloads/test-" + new Random().nextInt(100) + ".jpg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
