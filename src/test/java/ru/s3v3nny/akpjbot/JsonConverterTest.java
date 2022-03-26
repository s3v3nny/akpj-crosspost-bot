package ru.s3v3nny.akpjbot;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {

    String longPollServerTestResponse = """
            {"response":{"key":"blahblahSomeKey","server":"https:\\/\\/lp.vk.com\\/wh169644172","ts":"472"}}
            """;

    String postInfoTestResponse = """
            {
              "ts": "4",
              "updates": [
                {
                  "type": "wall_post_new",
                  "object": {
                    "id": 28,
                    "from_id": -123456,
                    "owner_id": -123456,
                    "date": 1519631591,
                    "marked_as_ads": 0,
                    "post_type": "post",
                    "text": "Post text",
                    "can_edit": 1,
                    "created_by": 564321,
                    "can_delete": 1,
                    "comments": {
                      "count": 0
                    }
                  },
                  "group_id": 123456
                }
              ]
            }
            """;


    public void setTestStringFromJsonFile () throws IOException {
        Path jsonFilePath = Path.of("C:/Users/s3v3n/IdeaProjects/akpj-crosspost/src/main/resources/sample.json");
        String jsonString = Files.readString(jsonFilePath);
        this.postInfoTestResponse = jsonString;
    }

    @Test
    public void testLongPollServerResponseParsing() {

        var parsedResponse = JsonConverter.longPollServerFromString(longPollServerTestResponse);

        assertEquals("blahblahSomeKey", parsedResponse.response.key);
        assertEquals("https://lp.vk.com/wh169644172", parsedResponse.response.server);

    }


    @Test
    public void testPostInfoFromStringResponseParsing() throws IOException {

        setTestStringFromJsonFile();
        var parsedResponse = JsonConverter.postInfoFromString(postInfoTestResponse);

        /*assertNotNull(parsedResponse.updates);
        assertEquals(1, parsedResponse.updates.size());
        assertEquals("Post text", parsedResponse.updates.get(0).object.text);
        assertEquals("wall_post_new", parsedResponse.updates.get(0).type);*/

        System.out.println(parsedResponse.updates.get(0).object.text);
        System.out.println(parsedResponse.updates.get(0).type);

        int index = 0;
        for(int i = 0; i < parsedResponse.updates.get(0).object.attachments.get(0).photo.sizes.size(); i++) {
            if("w".equals(parsedResponse.updates.get(0).object.attachments.get(0).photo.sizes.get(i).type)) index = i;
        }
        System.out.println(index);
        System.out.println(parsedResponse.updates.get(0).object.attachments.get(0).photo.sizes.get(index).url);
    }

    public void testPostInfoFailedResponse () throws IOException {

    }

}