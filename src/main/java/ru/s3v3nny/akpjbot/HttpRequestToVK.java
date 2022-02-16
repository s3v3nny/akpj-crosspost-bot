package ru.s3v3nny.akpjbot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}
