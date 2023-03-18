package io.bdan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class scraper {

    public static void scrape() {
        System.out.println(java.time.LocalDateTime.now() + "  |  Scraping deadmau5.com/shows...\n");
        try {
            Document doc = Jsoup.connect("https://deadmau5.com/shows").get();
            for(Element row : doc.select("div.showRow")) {
                final String date = row.select("div.showDate").text();
                final String venue = row.select("div.showVenue").text();
                final String location = row.select("div.showLocation").text();
                final String availability = row.select("div.showTickets").text();
                final String URL = row.select("a.ctaButton").attr("href");
                System.out.println(date + " | " + venue + " | " + location + " | " +  availability +  " | " + URL);
            }

            System.out.println("\n" + java.time.LocalDateTime.now() + "  |  Scraping complete...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}