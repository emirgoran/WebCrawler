package Translation;

import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

import static Translation.TranslationUtils.*;

public class TranslatorService {

    protected static final HashMap<String, String> TARGET_LANGUAGES = GetTargetLanguagesHashMap();
    protected static final HashMap<String, String> SOURCE_LANGUAGES = GetSourceLanguagesHashMap();

    public static TranslationResponse TranslateText(String originalTextArr[], String targetLanguageCode)
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

            return new TranslationResponse(originalTextArr, translatedTextArr, sourceLanguage, targetLanguage);
        }
        catch (Exception e) {
            throw new TranslationNotSuccessfulException();
        }
    }

    public static boolean IsSupportedTargetLanguageCode(String languageCode) {
        return TARGET_LANGUAGES.containsKey(languageCode);
    }

    public static String GetTargetLanguageNameByLanguageCode(String languageCode) {
        if (TARGET_LANGUAGES.containsKey(languageCode)) {
            return TARGET_LANGUAGES.get(languageCode);
        }

        return null;
    }

    public static String GetTargetLanguagesListFormatted() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : TARGET_LANGUAGES.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(key + ": " + value + "\n");
        }

        return sb.toString();
    }
}
