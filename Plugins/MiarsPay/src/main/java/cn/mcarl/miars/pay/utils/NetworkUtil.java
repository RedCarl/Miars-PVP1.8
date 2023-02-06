package cn.mcarl.miars.pay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NetworkUtil {
    public static String get(String strUrl, Map<String, Object> args) {
        StringBuilder urlBuilder = new StringBuilder(strUrl);
        if (args != null && !args.isEmpty()) {
            urlBuilder.append('?');
            args.forEach((k, v) -> {
                try {
                    String encode = URLEncoder.encode(v.toString(), "UTF-8");
                    urlBuilder.append(k).append('=').append(encode).append('&');
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        String urlString = urlBuilder.toString();
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String text;
            while ((text = bufferedReader.readLine()) != null)
                builder.append(text);
            return builder.toString();
        } catch (IOException ignored) {

        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
