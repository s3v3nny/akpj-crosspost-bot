package ru.s3v3nny.akpjbot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//String key, String server, String ts

public class HttpRequestToVK {
    public static String getPostInfo() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://lp.vk.com/wh212168415?act=a_check&key=7e968950f6751f197ee72a669807aca4eb47af62&wait=60&ts=3"))
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
