package Translation;

import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class TranslatorService implements Translator {

    protected HashMap<String, String> TARGET_LANGUAGES = GetTargetLanguagesHashMap();
    protected HashMap<String, String> SOURCE_LANGUAGES = GetSourceLanguagesHashMap();

    public Translation TranslateText(String originalTextArr[], String targetLanguageCode)
            throws TranslationNotSuccessfulException, TranslationInvalidArgumentException {
        if (originalTextArr == null || targetLanguageCode == null || !IsSupportedTargetLanguageCode(targetLanguageCode)) {
            throw new TranslationInvalidArgumentException();
        }

        try {
            Document translatedDocument = GetTranslatedDocument(originalTextArr, targetLanguageCode);

            JSONArray translationsJsonArr = new JSONObject(translatedDocument.text()).getJSONArray("translations");

            String[] translatedTextArr = ConvertTranslationsJsonArrayToStringArray(translationsJsonArr);
            String sourceLanguage = GetSourceLanguageFromTranslationJsonArray(translationsJsonArr);
            String targetLanguage = GetTargetLanguageNameByLanguageCode(targetLanguageCode);

            return new Translation(originalTextArr, translatedTextArr, sourceLanguage, targetLanguage);
        }
        catch (Exception e) {
            throw new TranslationNotSuccessfulException();
        }
    }

    public boolean IsSupportedTargetLanguageCode(String languageCode) {
        return TARGET_LANGUAGES.containsKey(languageCode);
    }

    public String GetTargetLanguageNameByLanguageCode(String languageCode) {
        if (TARGET_LANGUAGES.containsKey(languageCode)) {
            return TARGET_LANGUAGES.get(languageCode);
        }

        return null;
    }

    public String GetTargetLanguagesListFormatted() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : TARGET_LANGUAGES.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(key + ": " + value + "\n");
        }

        return sb.toString();
    }

    public String GetSourceLanguageNameByLanguageCode(String languageCode) {
        if (SOURCE_LANGUAGES.containsKey(languageCode)) {
            return SOURCE_LANGUAGES.get(languageCode);
        }

        return null;
    }

    protected String GetSourceLanguageFromTranslationJsonArray(JSONArray translationsJsonArr) {
        try {
            return GetSourceLanguageNameByLanguageCode(translationsJsonArr.getJSONObject(0).get("detected_source_language").toString());
        }
        catch (Exception e) {
            return null;
        }
    }

    /* STATIC PART */

    private final static String AUTH_KEY = "99bd2ffe-453c-f103-f71c-7d0233c0aa56:fx";
    private final static String API_URL_TRANSLATE = "https://api-free.deepl.com/v2/translate";
    private final static String API_URL_LANGUAGES = "https://api-free.deepl.com/v2/languages";

    static HashMap<String, String> GetTargetLanguagesHashMap() {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        Document languagesDocument = GetTargetLanguagesListDocument(true);

        if (languagesDocument == null) {
            return languagesHashMap;
        }

        return ConvertLanguagesJsonArrayToHashMap(new JSONArray(languagesDocument.text()));
    }

    static HashMap<String, String> GetSourceLanguagesHashMap() {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        Document languagesDocument = GetTargetLanguagesListDocument(false);

        if (languagesDocument == null) {
            return languagesHashMap;
        }

        return ConvertLanguagesJsonArrayToHashMap(new JSONArray(languagesDocument.text()));
    }

    private static Document GetTargetLanguagesListDocument(boolean isTarget) {
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

    static Document GetTranslatedDocument(String textsArr[], String targetLanguageCode) {
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

    private static HashMap<String, String> ConvertLanguagesJsonArrayToHashMap(JSONArray langArrJson) {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        for (int i = 0; i < langArrJson.length(); i++) {
            JSONObject langObjJson = langArrJson.getJSONObject(i);
            languagesHashMap.put(langObjJson.get("language").toString(), langObjJson.get("name").toString());
        }

        return languagesHashMap;
    }

    static String[] ConvertTranslationsJsonArrayToStringArray(JSONArray translationsJsonArr) {
        String[] translationTexts = new String[translationsJsonArr.length()];

        for (int i = 0; i < translationsJsonArr.length(); i++) {
            translationTexts[i] = translationsJsonArr.getJSONObject(i).get("text").toString();
        }

        return translationTexts;
    }
}
