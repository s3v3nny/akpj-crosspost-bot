package ru.s3v3nny.akpjbot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//String key, String server, String ts

// TODO: сделать методы, которые не используются в других классах, приватными
public class HttpRequestToVK {

    public static String getPostInfo(String key, String server, String ts) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server + "?act=a_check&key=" + key + "&wait=3600&ts=" + ts))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    // TODO: группу и токен перенести в аргументы
    public static String getLongPollServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.vk.com/method/groups.getLongPollServer?group_id=169644172&v=5.131&access_token=5a3a6fde6dc849a01decef984bad0bd69d6171c29a98264df3d30a7f59c75af7f5844eb921dd6c75bd4dc"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    // TODO: зачем каждый раз запрашивать новый лонгпулсервер? давай вынесем в поле и будем обновлять только если ошибка
    public static PostInfo getAndParseInfo() throws IOException, InterruptedException{
        PostInfo postInfo;
        String vkResponse = HttpRequestToVK.getLongPollServer();
        System.out.println("Long Poll Server Response: " + vkResponse);
        Response longPollServer = JsonConverter.longPollServerFromString(vkResponse);
        String postInfoString = HttpRequestToVK.getPostInfo(longPollServer.response.key, longPollServer.response.server, longPollServer.response.ts);
        System.out.println("Post Info Response: " + postInfoString);
        postInfo = JsonConverter.postInfoFromString(postInfoString);

        return postInfo;
    }
}
