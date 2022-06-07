package Translation;

import Data.Translation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

/* Not unit testable! Requires internet connection and authentication token to work. */
public class JsoupTranslatorApi implements TranslatorApi {

    // Please use your own. This one will be deactivated.
    private final static String AUTH_KEY = "99bd2ffe-453c-f103-f71c-7d0233c0aa56:fx";
    private final static String API_URL_TRANSLATE = "https://api-free.deepl.com/v2/translate";
    private final static String API_URL_LANGUAGES = "https://api-free.deepl.com/v2/languages";

    private HashMap<String, String> TARGET_LANGUAGES;
    private HashMap<String, String> SOURCE_LANGUAGES;

    public JsoupTranslatorApi() {
        TARGET_LANGUAGES = GetAvailableTargetLanguagesHashMap(true);
        SOURCE_LANGUAGES = GetAvailableTargetLanguagesHashMap(false);
    }

    private HashMap<String, String> GetAvailableTargetLanguagesHashMap(boolean isTarget) {
        try {
            Document document = Jsoup.connect(API_URL_LANGUAGES)
                    .ignoreContentType(true)
                    .data("auth_key", AUTH_KEY)
                    .data("type", isTarget ? "target" : "source")
                    .get();

            return GetLanguagesHashMap(new JSONArray(document.text()));
        }
        catch (Exception e) {
            return null;
        }
    }

    public Translation GetTranslation(String[] originalTextArr, String targetLanguageCode) {
        Connection connectionBuilder = Jsoup.connect(API_URL_TRANSLATE)
                .ignoreContentType(true)
                .cookie("auth_key", AUTH_KEY)
                .data("auth_key", AUTH_KEY)
                .data("target_lang", targetLanguageCode);

        for (int i = 0; i < originalTextArr.length; i++) {
            connectionBuilder.data("text", originalTextArr[i]);
        }

        try {
            JSONArray translationsJsonArr = new JSONObject(connectionBuilder.get().text()).getJSONArray("translations");

            String[] translatedTextArr = JsoupTranslatorApi.ConvertTranslationsJsonArrayToStringArray(translationsJsonArr);
            String sourceLanguage = GetSourceLanguageFromTranslationJsonArray(translationsJsonArr);
            String targetLanguage = GetTargetLanguageNameByLanguageCode(targetLanguageCode);

            return new Translation(originalTextArr, translatedTextArr, sourceLanguage, targetLanguage);
        }
        catch (Exception e) {
            return null;
        }
    }

    public HashMap<String, String> GetAvailableTargetLanguagesHashMap() {
        return TARGET_LANGUAGES;
    }

    @Override
    public HashMap<String, String> GetAvailableSourceLanguagesHashMap() {
        return SOURCE_LANGUAGES;
    }

    public String GetSourceLanguageNameByLanguageCode(String languageCode) {
        if (SOURCE_LANGUAGES.containsKey(languageCode)) {
            return SOURCE_LANGUAGES.get(languageCode);
        }

        return null;
    }

    public String GetTargetLanguageNameByLanguageCode(String languageCode) {
        if (TARGET_LANGUAGES.containsKey(languageCode)) {
            return TARGET_LANGUAGES.get(languageCode);
        }

        return null;
    }

    public String GetSourceLanguageFromTranslationJsonArray(JSONArray translationsJsonArr) {
        try {
            return GetSourceLanguageNameByLanguageCode(translationsJsonArr.getJSONObject(0).get("detected_source_language").toString());
        }
        catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, String> GetLanguagesHashMap(JSONArray languagesJsonArr) {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        if (languagesJsonArr == null) {
            return languagesHashMap;
        }

        return ConvertLanguagesJsonArrayToHashMap(languagesJsonArr);
    }

    public static HashMap<String, String> ConvertLanguagesJsonArrayToHashMap(JSONArray langArrJson) {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        for (int i = 0; i < langArrJson.length(); i++) {
            JSONObject langObjJson = langArrJson.getJSONObject(i);
            languagesHashMap.put(langObjJson.get("language").toString(), langObjJson.get("name").toString());
        }

        return languagesHashMap;
    }

    public static String[] ConvertTranslationsJsonArrayToStringArray(JSONArray translationsJsonArr) {
        String[] translationTexts = new String[translationsJsonArr.length()];

        for (int i = 0; i < translationsJsonArr.length(); i++) {
            translationTexts[i] = translationsJsonArr.getJSONObject(i).get("text").toString();
        }

        return translationTexts;
    }
}
