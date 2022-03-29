package ru.s3v3nny.akpjbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.s3v3nny.akpjbot.configs.*;
import ru.s3v3nny.akpjbot.models.telegram.*;
import ru.s3v3nny.akpjbot.models.vk.*;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    // TODO: задача со звездочкой:
    //  вынести весь код в отдельный класс, сделать все методы во всех остальных классах НЕ статичными
    //  в этом методе оставить только создание объекта класса в который переместишь код и вызов метода
    public static void main(String... args) {

        JsonConverter converter = new JsonConverter();
        HttpRequestToVK requestToVK = new HttpRequestToVK();

        ClassLoader classLoader = Main.class.getClassLoader();
        URL lpsURL = classLoader.getResource("LPSInfo.json");
        URL tgURL = classLoader.getResource("botinfo.json");

        File lpsFile = null;
        File tgFile = null;
        TelegramBotInfo tgBotInfo = null;
        LPSInfo lpsInfo = null;


        try {
            lpsFile = new File(lpsURL.toURI());
            tgFile = new File(tgURL.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Path lpsPath = lpsFile.toPath();
        Path tgPath = tgFile.toPath();

        try {
            lpsInfo = converter.lpsInfoFromString(Files.readString(lpsPath));
            tgBotInfo = converter.botInfoFromString(Files.readString(tgPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        TelegramBot bot = new TelegramBot(tgBotInfo.bot_token, tgBotInfo.chat_id, tgBotInfo.bot_name);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        do {
            Response response = requestToVK.getLongPollServerInfo(lpsInfo);
            PostInfo postInfo = requestToVK.getPostInfo(response);

            while (postInfo.failed != null) {
                postInfo = requestToVK.getPostInfo(response);
                response = requestToVK.getLongPollServerInfo(lpsInfo);
            }

            if (postInfo.updates.size() == 0) continue;

            if ("suggest".equals(postInfo.updates.get(0).object.post_type)) continue;

            Updates updates = postInfo.updates.get(0);
            Attachments attachments = postInfo.updates.get(0).object.attachments.get(0);


            if (updates.object.marked_as_ads == 1) continue;

            int index = 0;
            int maxResolution = 0;
            int width;
            int height;
            // TODO: задачка со звездочкой: почитать про Stream API, поэкспериментировать с переписыванием данного метода на Stream API
            for (int i = 0; i < attachments.photo.sizes.size(); i++) {
                width = attachments.photo.sizes.get(i).width;
                height = attachments.photo.sizes.get(i).height;
                if (width * height > maxResolution) {
                    index = i;
                    maxResolution = width * height;
                }

            }

            TelegramPostInfo tgPostInfo = new TelegramPostInfo();
            tgPostInfo.text = postInfo.updates.get(0).object.text;
            tgPostInfo.pictureURL = postInfo.updates.get(0).object.attachments.get(0).photo.sizes.get(index).url;

            try {
                bot.sendPic(tgPostInfo);
                System.out.println("Post has been send!");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        } while (true);


    }
}
