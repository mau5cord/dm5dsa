package io.bdan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.TimeUnit;

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
                String URL = row.select("a.ctaButton").attr("href");

                discord discord = new discord();
                discord.addEmbed(new discord.EmbedObject()
                        .setTitle(location)
                        .setDescription(venue + " — " + date + "\\n\\n["+ availability.toUpperCase() + "]("+ URL +")")
                        .setUrl("https://deadmau5.com/shows"));
                discord.execute();

                if(URL == "")
                    URL = "N/A";
                System.out.println(date + " | " + venue + " | " + location + " | " +  availability.toUpperCase() +  " | " + URL);
                TimeUnit.MILLISECONDS.sleep(100);

            }

            System.out.println("\n" + java.time.LocalDateTime.now() + "  |  Scraping complete...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}