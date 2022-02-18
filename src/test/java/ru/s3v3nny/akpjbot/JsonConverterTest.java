package ru.s3v3nny.akpjbot;

import org.junit.jupiter.api.Test;

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


    @Test
    public void testLongPollServerResponseParsing() {

        var parsedResponse = JsonConverter.longPollServerFromString(longPollServerTestResponse);

        assertEquals("blahblahSomeKey", parsedResponse.response.key);
        assertEquals("https://lp.vk.com/wh169644172", parsedResponse.response.server);

    }


    @Test
    public void testPostInfoFromStringResponseParsing() {

        var parsedResponse = JsonConverter.postInfoFromString(postInfoTestResponse);

        assertNotNull(parsedResponse.updates);
        assertEquals(1, parsedResponse.updates.length);
        assertEquals("Post text", parsedResponse.updates[0].object.text);
        assertEquals("wall_post_new", parsedResponse.updates[0].type);

    }

}