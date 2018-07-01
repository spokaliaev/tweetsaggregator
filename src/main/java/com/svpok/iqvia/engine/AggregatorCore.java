package com.svpok.iqvia.engine;

import com.svpok.iqvia.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AggregatorCore {

    private static Logger logger = LoggerFactory.getLogger(AggregatorCore.class);

    private RESTAPIClient restapiClient;

    public AggregatorCore(RESTAPIClient restapiClient) {
        this.restapiClient = restapiClient;
    }

    public List<Tweet> getAllTweetsInRange(Date from, Date to) {

        logger.info("Aggregation process is started");

        Map<Long, Tweet> result = new HashMap<Long, Tweet>();
        int previousIterationSize = 0;

        for (;;) {
            Tweet[] tweets = restapiClient.getTweetsInRange(from, to);

            for (Tweet tweet : tweets) {
                result.put(tweet.getId(), tweet);
            }

            if (previousIterationSize == result.size()) {
                break;
            }

            previousIterationSize = result.size();

            from = tweets[tweets.length - 1].getStamp();
        }

        logger.info("Aggregation process is ended with the size of aggregated tweets: " + result.size());

        List<Tweet> allTweets = new ArrayList<>(result.values());
        Collections.sort(allTweets, (o1, o2) -> o1.getStamp().getTime() > o2.getStamp().getTime() ? 1 : -1);
        return allTweets;
    }

}
