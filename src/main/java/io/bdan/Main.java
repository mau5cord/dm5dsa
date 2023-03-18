package io.bdan;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //this could definitely be done better, but it works so whatever 💀
        //and no, I don't want to use crontab leave me alone (me rn -> https://data.bdan.io/img/angy.jpeg)
        boolean run = true;
        while(run){
            scraper.scrape();
            System.out.println(java.time.LocalDateTime.now() + "  |  Waiting 1 Minute...");
            TimeUnit.MINUTES.sleep(1);
        }
    }
}