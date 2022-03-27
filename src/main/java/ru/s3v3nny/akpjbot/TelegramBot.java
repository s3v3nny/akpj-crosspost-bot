package ru.s3v3nny.akpjbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TelegramBot extends TelegramLongPollingBot {

    // TODO: вынести в конструктор
    private String botToken = "5185813624:AAERkCPJ8OUt-0WA_iCyPDSo_biCy4qKX3E";
    private String chatId = "-1001714989081";
    private String botName = "akpj_bot";


    public void onUpdateReceived(Update update) {
    }

    public synchronized void sendMsg(String chatId, TelegramPostInfo postInfo) {

    }

    // TODO: зачем synchronized? удалять не надо, надо объяснить зачем
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
