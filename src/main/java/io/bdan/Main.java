package io.bdan;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
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
            System.out.println(java.time.LocalDateTime.now() + "  |  waiting 1 Minute...");
            TimeUnit.MINUTES.sleep(1);
        }
        Scraper.scrape(testChannel);
    }
}