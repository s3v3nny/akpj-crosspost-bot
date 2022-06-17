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
import java.util.Comparator;
import java.util.List;


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
        } catch (URISyntaxException | NullPointerException e) {
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
            Response response = requestToVK.parseLongPollServerInfo(lpsInfo);
            if (response.response.ts == null || response.response.key == null || response.response.server == null)
                continue;
            PostInfo postInfo = requestToVK.parsePostInfo(response);

            while (postInfo.failed != null) {
                postInfo = requestToVK.parsePostInfo(response);
                response = requestToVK.parseLongPollServerInfo(lpsInfo);
            }


            if (postInfo.updates.size() == 0) continue;
            if ("suggest".equals(postInfo.updates.get(0).object.post_type)) continue;
            if (postInfo.updates.get(0).object == null) continue;
            if (postInfo.updates.get(0).object.attachments == null) continue;
            if (postInfo.updates.get(0).object.attachments.get(0).photo == null) continue;


            Updates updates = postInfo.updates.get(0);
            List<Sizes> sizes = postInfo.updates.get(0).object.attachments.get(0).photo.sizes;

            if (updates.object.marked_as_ads == 1) continue;


            List<Sizes> sortedSizes = sizes.stream()
                    .peek(s1 -> s1.setResolution(s1.width, s1.height))
                    .sorted(Comparator.comparing(Sizes::getResolution).reversed())
                    .toList();

            TelegramPostInfo tgPostInfo = new TelegramPostInfo();
            tgPostInfo.text = postInfo.updates.get(0).object.text;
            tgPostInfo.pictureURL = sortedSizes.get(0).url;

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
