package com.svpok.iqvia;

import com.svpok.iqvia.engine.AggregatorCore;
import com.svpok.iqvia.engine.RESTAPIClient;
import com.svpok.iqvia.exceptions.FileSystemException;
import com.svpok.iqvia.model.Tweet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

public class TweetsAggregator {

    private static AggregatorCore aggregatorCore;

    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "config.properties";
            input = TweetsAggregator.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            aggregatorCore = new AggregatorCore(new RESTAPIClient(prop.getProperty("api-url")));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        updateTweetsInOutputFile();

    }

    private static void updateTweetsInOutputFile() {

        Calendar from = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar to = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        from.set(2016, 0, 1, 0, 0, 0);
        to.set(2018, 0,1, 0, 0, 0);

        List<Tweet> tweets = aggregatorCore.getAllTweetsInRange(from.getTime(), to.getTime());

        File file = new File("output.csv");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        //Create the file
        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            for (Tweet tweet : tweets) {
                writer.write(tweet.getId() + ";" + sdf.format(tweet.getStamp()) + ";...\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new FileSystemException("Exception during a file creation process");
        }
    }

}