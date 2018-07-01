package com.svpok.iqvia.engine;

import com.svpok.iqvia.model.Tweet;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class AggregatorCoreTest {

    private AggregatorCore aggregatorCore;

    @Before
    public void initialization() {
        aggregatorCore = new AggregatorCore(new RESTAPIClient("https://badapi.iqvia.io/api/v1/Tweets"));
    }

    @Test
    public void testGetAllTweetsInRange() {

        Calendar from = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar to = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        from.set(2016, 0, 1, 0, 0, 0);
        to.set(2018, 0,1, 0, 0, 0);

        List<Tweet> tweets = aggregatorCore.getAllTweetsInRange(from.getTime(), to.getTime());

        assertEquals(11693, tweets.size());
    }

}
