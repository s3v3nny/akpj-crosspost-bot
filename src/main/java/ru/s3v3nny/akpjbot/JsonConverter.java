package ru.s3v3nny.akpjbot;

import com.google.gson.Gson;
import ru.s3v3nny.akpjbot.models.vk.*;
import ru.s3v3nny.akpjbot.configs.*;

public class JsonConverter {

    private static final Gson gson = new Gson();

    public Response responseInfoFromString (String jsonString) {
        return gson.fromJson(jsonString, Response.class);
    }

    public PostInfo postInfoFromString(String jsonString) {
        return gson.fromJson(jsonString, PostInfo.class);
    }

    public TelegramBotInfo botInfoFromString(String botInfo) {
        return gson.fromJson(botInfo, TelegramBotInfo.class);
    }

    public LPSInfo lpsInfoFromString (String lpsInfo) {
        return gson.fromJson(lpsInfo, LPSInfo.class);
    }
}
