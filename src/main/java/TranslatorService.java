import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TranslatorService {

    public static void main(String[] args) throws IOException {
        TranslateText("Hello", "DE");
    }

    private final static String AUTH_KEY = "99bd2ffe-453c-f103-f71c-7d0233c0aa56:fx";

    public static String TranslateText(String text, String targetLanguage) throws IOException {
        Document translatedDocument = Jsoup.connect("https://api-free.deepl.com/v2/translate")
                .ignoreContentType(true)
                .cookie("auth_key", AUTH_KEY)
                .data("auth_key", AUTH_KEY)
                .data("target_lang", "DE")
                .data("text", "Good day!")
                .data("text", "Hello!")
                .get();

        System.out.println(translatedDocument.text());

        JSONArray translationsArrayJson = new JSONObject(translatedDocument.text()).getJSONArray("translations");

        for (int i = 0; i < translationsArrayJson.length(); i++) {
            JSONObject translationObjectJson = translationsArrayJson.getJSONObject(i);

            System.out.println(translationObjectJson.get("text"));
        }


        return null;
    }
}
