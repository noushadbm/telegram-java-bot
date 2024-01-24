package com.rayshan.bot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class BotService extends TelegramLongPollingBot {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

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
                if (photos.size() > 0) {
                    PhotoSize photo = photos.get(3);
                    String fileId = photo.getFileId();
                    downloadFileToLocal(fileId, "audio", ".jpg");
                }
            } else if (inMessage.hasVoice()) {
                log.info("Recorded Voice present....");
                Voice voice = inMessage.getVoice();
                //log.info("File mime type: {}", voice.getMimeType());
                downloadFileToLocal(voice.getFileId(), "voice", ".ogg");

            } else if (inMessage.hasAudio()) {
                log.info("Audio present....");
                Audio audio = inMessage.getAudio();
                //log.info("File mime type: {}", audio.getMimeType());
                log.info("File name: {}", audio.getFileName());
                downloadFileToLocal(audio.getFileId(), audio.getFileName(), ".mp3");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File downloadFileToLocal(String fileId, String prefix, String fileExtension) throws TelegramApiException {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        String filePath = execute(getFileRequest).getFilePath();
        String fileName = LocalDateTime.now().format(FORMATTER);
        File newFile = downloadFile(filePath, new File("downloads/" + prefix + "-" + fileName + fileExtension));
        log.info("File downloaded. File : {}", newFile.getAbsolutePath());
        return newFile;
    }
}
