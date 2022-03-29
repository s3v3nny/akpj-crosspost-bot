package ru.s3v3nny.akpjbot;

import org.checkerframework.checker.units.qual.K;
import ru.s3v3nny.akpjbot.configs.LPSInfo;
import ru.s3v3nny.akpjbot.models.vk.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


// TODO: сделать методы, которые не используются в других классах, приватными
public class HttpRequestToVK {

    private final JsonConverter converter = new JsonConverter();

    private String getPost(String key, String server, String ts) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server + "?act=a_check&key=" + key + "&wait=3600&ts=" + ts))
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

    private String getLongPollServer(String token, String groupId) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.vk.com/method/groups.getLongPollServer?group_id=" + groupId + "&v=5.131&access_token=" + token))
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

    // TODO: зачем каждый раз запрашивать новый лонгпулсервер? давай вынесем в поле и будем обновлять только если ошибка
    public PostInfo getPostInfo(Response lPS)  {

        String postInfoString = getPost(lPS.response.key, lPS.response.server, lPS.response.ts);

        System.out.println("Post Info Response: " + postInfoString);
        return converter.postInfoFromString(postInfoString);
    }

    public Response getLongPollServerInfo(LPSInfo lpsInfo) {

        String vkResponse = getLongPollServer(lpsInfo.token, lpsInfo.group_id);

        System.out.println("LPS Response: " + vkResponse);
        return converter.responseInfoFromString(vkResponse);
    }
}
