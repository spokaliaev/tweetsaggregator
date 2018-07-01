package com.svpok.iqvia.engine;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svpok.iqvia.exceptions.APIConnectionException;
import com.svpok.iqvia.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RESTAPIClient {

    private static Logger logger = LoggerFactory.getLogger(RESTAPIClient.class);

    private final String URL;
    private final ObjectMapper objectMapper;
    private final SimpleDateFormat sdf;

    public RESTAPIClient(String url) {
        this.URL = url;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);


    }

    public Tweet[] getTweetsInRange(Date from, Date to) {

        String tweetsJson = requestTweets(from, to);

        try {
            Tweet[] tweets = objectMapper.readValue(tweetsJson, Tweet[].class);
            logger.info("Got " + tweets.length + " tweets");
            return tweets;
        } catch (IOException e) {
            throw new APIConnectionException("Result format exception");
        }
    }

    private String requestTweets(Date from, Date to) {

        try {
            String fromDateStr = sdf.format(from);
            String toDateStr = sdf.format(to);

            URL obj = new URL(URL + "?startDate=" + fromDateStr + "&endDate=" + toDateStr);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            logger.info("Getting tweets in the range: " + fromDateStr + " " + toDateStr);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                return response.toString();
            }
        } catch (IOException e) {
            // If there are problems with exception, application's exception (APIConnectionException) will be thrown.
        }

        throw new APIConnectionException("Http connection exception");
    }

}
