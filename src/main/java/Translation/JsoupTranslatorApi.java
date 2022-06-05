package Translation;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/* Not unit testable! Requires internet connection and authentication token to work. */
public class JsoupTranslatorApi implements TranslatorApi {

    // Please use your own. This one will be deactivated.
    private final static String AUTH_KEY = "99bd2ffe-453c-f103-f71c-7d0233c0aa56:fx";
    private final static String API_URL_TRANSLATE = "https://api-free.deepl.com/v2/translate";
    private final static String API_URL_LANGUAGES = "https://api-free.deepl.com/v2/languages";

    public Document GetLanguagesListDocument(boolean isTarget) {
        try {
            return Jsoup.connect(API_URL_LANGUAGES)
                    .ignoreContentType(true)
                    .data("auth_key", AUTH_KEY)
                    .data("type", isTarget ? "target" : "source")
                    .get();
        }
        catch (Exception e) {
            return null;
        }
    }

    public Document GetTranslatedDocument(String textsArr[], String targetLanguageCode) {
        Connection connectionBuilder = Jsoup.connect(API_URL_TRANSLATE)
                .ignoreContentType(true)
                .cookie("auth_key", AUTH_KEY)
                .data("auth_key", AUTH_KEY)
                .data("target_lang", targetLanguageCode);

        for (int i = 0; i < textsArr.length; i++) {
            connectionBuilder.data("text", textsArr[i]);
        }

        try {
            return connectionBuilder.get();
        }
        catch (Exception e) {
            return null;
        }
    }
}
