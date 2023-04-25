package ru.s3v3nny.akpjbot;

import ru.s3v3nny.akpjbot.configs.LPSInfo;
import ru.s3v3nny.akpjbot.models.vk.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class HttpRequestToVK {

    private final JsonConverter converter = new JsonConverter();

    private String getPostInfo(String key, String server, String ts) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server + "?act=a_check&key=" + key + "&wait=3600&ts=" + ts))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return response.body();

    }

    private String getLongPollServerInfo(String token, String groupId) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.vk.com/method/groups.getLongPollServer?group_id=" + groupId + "&v=5.131&access_token=" + token))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return response.body();

    }

    public PostInfo parsePostInfo(Response lPS) {

        String postInfoString = getPostInfo(lPS.response.key, lPS.response.server, lPS.response.ts);

        System.out.println("Post Info Response: " + postInfoString);
        return converter.postInfoFromString(postInfoString);
    }

    public Response parseLongPollServerInfo(LPSInfo lpsInfo) {

        String vkResponse = getLongPollServerInfo(lpsInfo.token, lpsInfo.group_id);

        System.out.println("LPS Response: " + vkResponse);
        return converter.responseInfoFromString(vkResponse);
    }
}
