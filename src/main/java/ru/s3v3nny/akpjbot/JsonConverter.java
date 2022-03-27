package ru.s3v3nny.akpjbot;

import com.google.gson.Gson;

public class JsonConverter {

    private static final Gson gson = new Gson();

    // TODO: вынести gson в поле, чтобы не создавать новый каждый раз - +
    public static Response longPollServerFromString(String jsonString){
        return gson.fromJson(jsonString, Response.class);
    }

    public static PostInfo postInfoFromString(String jsonString) {

        return gson.fromJson(jsonString, PostInfo.class);
    }
}
