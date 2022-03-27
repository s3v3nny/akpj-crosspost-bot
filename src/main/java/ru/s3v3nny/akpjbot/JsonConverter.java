package ru.s3v3nny.akpjbot;

import com.google.gson.Gson;

public class JsonConverter {

    // TODO: вынести gson в поле, чтобы не создавать новый каждый раз
    public static Response longPollServerFromString(String jsonString){
        Gson gson = new Gson();
        Response connectInfo = gson.fromJson(jsonString, Response.class);
        return connectInfo;
    }

    public static PostInfo postInfoFromString(String jsonString) {
        Gson gson = new Gson();
        PostInfo postInfo = gson.fromJson(jsonString, PostInfo.class);
        return postInfo;
    }
}
