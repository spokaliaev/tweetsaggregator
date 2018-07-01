package com.svpok.iqvia.engine;

import com.svpok.iqvia.model.Tweet;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class RESTAPIClientTest {

    private RESTAPIClient restapiClient;

    @Before
    public void initialization() {
        restapiClient = new RESTAPIClient("https://badapi.iqvia.io/api/v1/Tweets");
    }


    @Test
    public void testTweetsInRange() {

        Calendar from = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar to = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        from.set(2016, 0, 1, 0, 0, 0);
        to.set(2016, 0,2, 0, 0, 0);

        Tweet[] tweets = restapiClient.getTweetsInRange(from.getTime(), to.getTime());

        assertEquals(19, tweets.length);
    }
}
