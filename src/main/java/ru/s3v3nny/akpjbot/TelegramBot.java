package ru.s3v3nny.akpjbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.s3v3nny.akpjbot.models.telegram.TelegramPostInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String chatId;
    private final String botName;

    public TelegramBot(String botToken, String chatId, String botName) {
        this.botName = botName;
        this.chatId = chatId;
        this.botToken = botToken;
    }


    public void onUpdateReceived(Update update) {}

    public synchronized void sendMsg() {}

    public synchronized void sendPic(TelegramPostInfo postInfo) throws IOException {
        String chatId = this.chatId;
        URL pictureURL = new URL(postInfo.pictureURL);
        InputStream inputStream = (pictureURL).openStream();

        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        InputFile inputFile = new InputFile(inputStream, "nameazaza");
        sendPhotoRequest.setPhoto(inputFile);
        sendPhotoRequest.setCaption(postInfo.text);

        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public String getBotUsername() {
        return this.botName;
    }

    public String getBotToken() {
        return this.botToken;
    }
}
