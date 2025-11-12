package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleTranslateService {
    private static final String API_URL = "https://translate.googleapis.com/translate_a/single";
    private static final int TIMEOUT = 5000;

    public String translateWord(String englishWord) {
        return translateText(englishWord);
    }

    public String translatePhrase(String englishPhrase) {
        return translateText(englishPhrase);
    }

    private String translateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String urlStr = API_URL + "?client=gtx&sl=en&tl=uk&dt=t&q=" + encodedText;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return parseTranslation(response.toString());
            } else {
                System.out.println("Помилка при зверненні до Google Translate: " + responseCode);
                return alternativeTranslate(text);
            }
        } catch (Exception e) {
            System.out.println("Помилка при перекладі через Google: " + e.getMessage());
            return alternativeTranslate(text);
        }
    }

    private String parseTranslation(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            JSONArray translationsArray = jsonArray.getJSONArray(0);
            StringBuilder translatedText = new StringBuilder();
            for (int i = 0; i < translationsArray.length(); i++) {
                JSONArray translationPart = translationsArray.getJSONArray(i);
                translatedText.append(translationPart.getString(0));
            }
            return translatedText.toString();
        } catch (Exception e) {
            System.out.println("Помилка при парсингу відповіді: " + e.getMessage());
            return alternativeTranslate(jsonResponse);
        }
    }

    private String alternativeTranslate(String text) {
        try {
            String urlStr = "https://libretranslate.com/translate";
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoOutput(true);
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            String jsonInputString = String.format(
                    "{\"q\":\"%s\",\"source\":\"en\",\"target\":\"uk\",\"format\":\"text\"}",
                    text.replace("\"", "\\\"")
            );
            connection.getOutputStream().write(jsonInputString.getBytes("UTF-8"));
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.optString("translatedText", text);
            }
        } catch (Exception e) {
            System.out.println("Помилка при альтернативному перекладі: " + e.getMessage());
        }
        return text;
    }
}