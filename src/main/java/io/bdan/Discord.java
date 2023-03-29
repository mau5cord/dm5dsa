package io.bdan;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Discord {
    private List<EmbedObject> embeds = new ArrayList<>();
    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute(boolean testChannel) throws IOException, InterruptedException {
        String channel = "https://discord.com/api/webhooks/";
        String text = ""; //kody said no ping role :(
        if (testChannel == true) {
            channel = "https://discord.com/api/webhooks/";
            text = "running in test mode..." + java.time.LocalDateTime.now();
        }

        System.out.println(java.time.LocalDateTime.now() + "  |  Sending to Discord...");
        TimeUnit.MILLISECONDS.sleep(500); //To prevent rate limiting
        JSONObject json = new JSONObject();
        json.put("content", text);
        List<JSONObject> embedObjects = new ArrayList<>();

        for (EmbedObject embed : this.embeds) {
            JSONObject jsonEmbed = new JSONObject();
            jsonEmbed.put("title", embed.getTitle());
            jsonEmbed.put("description", embed.getDescription());
            jsonEmbed.put("url", "https://deadmau5.com/shows");
            jsonEmbed.put("color", "000000");
            embedObjects.add(jsonEmbed);
        }
        json.put("embeds", embedObjects.toArray());
        URL url = new URL(channel);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "java");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();
        connection.getInputStream().close();
        connection.disconnect();
        System.out.println(java.time.LocalDateTime.now() + "  |  sent, done...");
    }
    public static class EmbedObject {
        private String title;
        private String description;
        public String getTitle() {
            return title;
        }
        public String getDescription() {
            return description;
        }
        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }
        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }
    }

    private class JSONObject {
        private final HashMap<String, Object> map = new HashMap<>();
        void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val.toString());
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);

                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }
                builder.append(++i == entrySet.size() ? "}" : ",");
            }
            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }
}