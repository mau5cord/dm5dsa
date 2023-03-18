package io.bdan;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class scraper {
    public static void scrape() {
        System.out.println("\n" + java.time.LocalDateTime.now() + "  |  Scraping deadmau5.com/shows...\n");
        ArrayList <JSONObject> jsonList = new ArrayList<JSONObject>();
        try {
            Document doc = Jsoup.connect("https://deadmau5.com/shows").get();
            int i = 0;
            for(Element row : doc.select("div.showRow")) {
                JSONObject json = new JSONObject();
                i++;
                final String date = row.select("div.showDate").text();
                final String venue = row.select("div.showVenue").text();
                final String location = row.select("div.showLocation").text();
                final String availability = row.select("div.showTickets").text();
                final String URL = row.select("a.ctaButton").attr("href");
                json.put("date", date);
                json.put("venue", venue);
                json.put("location", location);
                json.put("availability", availability);
                json.put("URL", URL);
                jsonList.add(json);
                if(URL == "")
                    System.out.println(java.time.LocalDateTime.now() + "  |  row #" + i + "  |  " + date + " | " + venue + " | " + location + " | " +  availability.toUpperCase() +  " | " + "N/A");
                else
                    System.out.println(java.time.LocalDateTime.now() + "  |  row #" + i + "  |  " + date + " | " + venue + " | " + location + " | " +  availability.toUpperCase() +  " | " + URL);
                discord discord = new discord();
                discord.addEmbed(new discord.EmbedObject().setTitle(location).setDescription(venue + " — " + date + "\\n\\n["+ availability.toUpperCase() + "]("+ URL +")"));
                discord.execute();
                TimeUnit.MILLISECONDS.sleep(250);
            }
            System.out.println(java.time.LocalDateTime.now() + "  |  Scraping complete...");
            try {
                mkJSON(jsonList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mkJSON(ArrayList<JSONObject> jsonList) throws IOException {
        //I know it's not generating a valid JSON file, buzz off.
        //No one is going to read this anyway.
        System.out.println(java.time.LocalDateTime.now() + "  |  Writing file...");
        FileWriter file = new FileWriter("shows.json");
        for(JSONObject obj : jsonList){
            file.append(obj.toJSONString());
            file.append("\n");
        }
        file.close();
        System.out.println(java.time.LocalDateTime.now() + "  |  Done...");
    }
}