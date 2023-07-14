/*
 *     Copyright 2016-2017 SparklingComet @ http://shanerx.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.mcarl.miars.storage.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Mojang {
    static private final JsonParser parser = new JsonParser();
    static private final String API_PROFILE_LINK = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public Mojang() {}

    public String getUUIDOfUsername(String username) {
        JsonObject object=(JsonObject) new JsonParser().parse(HttpClient.doGet("https://api.mojang.com/users/profiles/minecraft/" + username));
        return object.get("id").getAsString();
    }

    public String getPlayerHead(String uuid) {
        JsonObject object=(JsonObject) new JsonParser().parse(HttpClient.doGet(API_PROFILE_LINK + uuid));
        return object.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
    }

    public static String getSkinUrl(String uuid){
        String json = getContent(API_PROFILE_LINK + uuid);
        assert json != null;
        JsonObject o = parser.parse(json).getAsJsonObject();
        String jsonBase64 = o.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

        o = parser.parse(new String(Base64.decodeBase64(jsonBase64))).getAsJsonObject();
        return o.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
    }
    public static String getContent(String link){
        try {
            URL url = new URL(link);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder outputLine = new StringBuilder();

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                outputLine.append(inputLine);
            }
            br.close();
            return outputLine.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBaseHead(String basehead){
        JsonObject o = parser.parse(new String(Base64.decodeBase64(basehead))).getAsJsonObject();
        return o.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
    }
}