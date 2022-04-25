package Translation;

import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class TranslatorService implements Translator {

    protected HashMap<String, String> TARGET_LANGUAGES;
    protected HashMap<String, String> SOURCE_LANGUAGES;
    TranslatorApi translatorApi;

    public TranslatorService(TranslatorApi translatorApi) {
        TARGET_LANGUAGES = GetLanguagesHashMap(translatorApi.GetLanguagesListDocument(true));
        SOURCE_LANGUAGES = GetLanguagesHashMap(translatorApi.GetLanguagesListDocument(false));
        this.translatorApi = translatorApi;
    }

    public Translation TranslateText(String originalTextArr[], String targetLanguageCode)
            throws TranslationNotSuccessfulException, TranslationInvalidArgumentException {
        if (originalTextArr == null || targetLanguageCode == null || !IsSupportedTargetLanguageCode(targetLanguageCode)) {
            throw new TranslationInvalidArgumentException();
        }

        try {
            Document translatedDocument = translatorApi.GetTranslatedDocument(originalTextArr, targetLanguageCode);

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

    public String GetSourceLanguageFromTranslationJsonArray(JSONArray translationsJsonArr) {
        try {
            return GetSourceLanguageNameByLanguageCode(translationsJsonArr.getJSONObject(0).get("detected_source_language").toString());
        }
        catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, String> GetLanguagesHashMap(Document languagesDocument) {
        HashMap<String, String> languagesHashMap = new HashMap<>();

        if (languagesDocument == null) {
            return languagesHashMap;
        }

        return ConvertLanguagesJsonArrayToHashMap(new JSONArray(languagesDocument.text()));
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
