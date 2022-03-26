package ru.s3v3nny.akpjbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;


public class Main {
    public static void main(String... args) throws IOException, InterruptedException {
        //String vkResponse = HttpRequestToVK.getLongPollServer();
        //System.out.println(vkResponse);
        //Response longPollServer = JsonConverter.longPollServerFromString(vkResponse);


        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        String postInfoString = HttpRequestToVK.getPostInfo();
        PostInfo postInfo = JsonConverter.postInfoFromString(postInfoString);

        int index = 0;
        int maxResolution = 0;
        int width;
        int height;
        for (int i = 0; i < postInfo.updates.get(0).object.attachments.get(0).photo.sizes.size(); i++) {
            width = postInfo.updates.get(0).object.attachments.get(0).photo.sizes.get(i).width;
            height = postInfo.updates.get(0).object.attachments.get(0).photo.sizes.get(i).height;
            if (width * height > maxResolution) {
                index = i;
                maxResolution = width * height;
            }
        }

        TelegramPostInfo tgPostInfo = new TelegramPostInfo();
        tgPostInfo.text = postInfo.updates.get(0).object.text;
        tgPostInfo.pictureURL = postInfo.updates.get(0).object.attachments.get(0).photo.sizes.get(index).url;

        TelegramBot bot = new TelegramBot();

        bot.sendPic(tgPostInfo);

    }
}
