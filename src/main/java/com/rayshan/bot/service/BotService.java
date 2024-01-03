package com.rayshan.bot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.File;

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
        //System.out.println(update.getMessage());
        //System.out.println(update.getMessage().getText());

        if (update.getMessage().hasText()) {
            String incomingMsg = update.getMessage().getText();
            log.info("Received message {}", incomingMsg);

            if (incomingMsg.equalsIgnoreCase("/image")) {
                // Send sample png file.
                try {
                    InputFile inputFile = new InputFile(new File("Sample.png"));
                    SendPhoto message = new SendPhoto();
                    message.setPhoto(inputFile);
                    message.setChatId(update.getMessage().getChatId().toString());
                    execute(message); // Call method to send the message
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(update.getMessage().getText());

                try {
                    message.setChatId(update.getMessage().getChatId().toString());
                    execute(message); // Call method to send the message
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
