package io.bdan;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        boolean testChannel = false;
        boolean loop = true;

        for (String s: args) {
            if (s.contains("-testChannel")) {
                System.out.println(java.time.LocalDateTime.now() + "  |  running in test mode");
                testChannel = true;
            }
            if (s.contains("-noLoop")) {
                System.out.println(java.time.LocalDateTime.now() + "  |  running in no loop mode");
                loop = false;
            }
        }

        //this could definitely be done better, but it works so whatever 💀
        //and no, I don't want to use crontab leave me alone (me rn -> https://data.bdan.io/img/angy.jpeg)
        while(loop){
            Scraper.scrape(testChannel);
            System.out.println(java.time.LocalDateTime.now() + "  |  waiting 5 minutes...");
            TimeUnit.MINUTES.sleep(5);
        }
        Scraper.scrape(testChannel);
    }
}