package ru.s3v3nny.akpjbot;

import java.io.IOException;


public class Main {
    public static void main (String ...args)  throws IOException, InterruptedException {
        String vkResponse = HttpRequestToVK.getLongPollServer();
        System.out.println(vkResponse);
        Response longPollServer = JsonConverter.longPollServerFromString(vkResponse);
        String postInfoString = HttpRequestToVK.getPostInfo(longPollServer.response.key, longPollServer.response.server, longPollServer.response.ts);
        PostInfo postInfo = JsonConverter.postInfoFromString(postInfoString);
        System.out.println(postInfo.updates[0].object.text);
        int index = postInfo.updates[0].object.attachments.sizes.indexOf("w");
        System.out.println(postInfo.updates[0].object.attachments.sizes.get(index).height);
        System.out.println(postInfo.updates[0].object.attachments.sizes.get(index).width);
        System.out.println(postInfo.updates[0].object.attachments.sizes.get(index).url);


        System.out.println(postInfoString);

    }
}
