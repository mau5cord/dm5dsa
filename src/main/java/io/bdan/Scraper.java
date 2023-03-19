package io.bdan;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class Scraper {

    public static void scrape(boolean testChannel) {
        System.out.println("\n" + java.time.LocalDateTime.now() + "  |  scraping deadmau5.com/shows...");
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
                String URL = row.select("a.ctaButton").attr("href");
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
            }
            System.out.println(java.time.LocalDateTime.now() + "  |  scraping complete...");
            try {
                mkJSON(testChannel, jsonList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList <JSONObject> compareFiles(Path x, ArrayList<JSONObject> jL) {
        ArrayList <JSONObject> list = new ArrayList<JSONObject>();
        ArrayList <JSONObject> ausList = new ArrayList<JSONObject>();
        try {
            BufferedReader bf1 = Files.newBufferedReader(x);

            String line1 = "";
            JSONObject json = null;
            while ((line1 = bf1.readLine()) != null) {
                Map map = new Gson().fromJson(line1, Map.class);
                json = new JSONObject(map);
                list.add(json);
            }
            for (JSONObject obj : jL) {
                if (!list.contains(obj)) {
                    ausList.add(obj);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ausList;
    }

    private static void mkJSON(boolean testChannel, ArrayList<JSONObject> jsonList) throws IOException, InterruptedException {
        //I know it's not generating a valid JSON file, buzz off.
        //No one is going to read this anyway.
        System.out.println(java.time.LocalDateTime.now() + "  |  writing file...");
        File show = new File("shows.json");
        File show1 = new File("shows1.json");
        if (!show.exists()) {
            System.out.println(java.time.LocalDateTime.now() + "  |  no new shows found...");
            printFile(show.getName(), jsonList);
            discordList(testChannel, jsonList);
        } else {

            ArrayList<JSONObject> jsonList1 = compareFiles(show.toPath(), jsonList);
            if (jsonList1.size() > 0) {
                System.out.println(java.time.LocalDateTime.now() + "  |  new shows found...");
                discordList(testChannel, jsonList1);
                show.delete();
                printFile(show.getName(), jsonList);
            }
        }
    }

    private static void discordList(boolean testChannel, ArrayList<JSONObject> jsonList) throws IOException, InterruptedException {
        for (JSONObject obj : jsonList) {
            Discord discord = new Discord();
            discord.addEmbed(new Discord.EmbedObject().setTitle(obj.get("location").toString()).setDescription(obj.get("venue").toString() + " — " + obj.get("date").toString() + "\\n\\n["+ obj.get("availability").toString().toUpperCase() + "]("+ obj.get("URL").toString() +")"));
            discord.execute(testChannel);
        }
    }

    private static void printFile(String fileName, ArrayList<JSONObject> jsonList) throws IOException {
        FileWriter file = new FileWriter(fileName);

        for (JSONObject obj : jsonList) {
            file.append(obj.toJSONString());
            file.append("\n");
        }
        file.close();
        System.out.println(java.time.LocalDateTime.now() + "  |  done...");
    }
}