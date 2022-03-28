package ru.s3v3nny.akpjbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;


public class Main {

    // TODO: задача со звездочкой:
    //  вынести весь код в отдельный класс, сделать все методы во всех остальных классах НЕ статичными
    //  в этом методе оставить только создание объекта класса в который переместишь код и вызов метода
    public static void main(String... args) throws IOException, InterruptedException {


        TelegramBot bot = new TelegramBot();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        do {

            PostInfo postInfo = HttpRequestToVK.getAndParseInfo();

            while(postInfo.failed != null)
                postInfo = HttpRequestToVK.getAndParseInfo();

            if (postInfo.updates.size() == 0) continue;

            Updates updates = postInfo.updates.get(0);
            Attachments attachments = postInfo.updates.get(0).object.attachments.get(0);

            if("suggest".equals(updates.object.post_type)) continue;

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

            bot.sendPic(tgPostInfo);
            System.out.println("картин очка отправлена!");

        } while (true);


    }
}
