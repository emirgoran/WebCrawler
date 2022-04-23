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

    protected static final HashMap<String, String> LANGUAGES = GetLanguagesHashMap();

    public static void main(String[] args) throws TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        var translation = TranslateText(new String[] {"Hello guys!", "What's up?"}, "IT");

        System.out.println(translation.getSourceLanguage());
        System.out.println(translation.getTargetLanguage());
    }

    public static TranslationResponse TranslateText(String originalTextArr[], String targetLanguageCode)
            throws TranslationNotSuccessfulException, TranslationInvalidArgumentException {
        if (originalTextArr == null || targetLanguageCode == null || !IsSupportedLanguageCode(targetLanguageCode)) {
            throw new TranslationInvalidArgumentException();
        }

        try {
            Document translatedDocument = GetTranslatedDocument(originalTextArr, targetLanguageCode);

            if (translatedDocument == null) {
                throw new TranslationNotSuccessfulException();
            }

            System.out.println(translatedDocument.text());
            JSONArray translationsJsonArr = new JSONObject(translatedDocument.text()).getJSONArray("translations");
            String[] translatedTextArr = ConvertTranslationsJsonArrayToStringArray(translationsJsonArr);
            String sourceLanguage = GetLanguageFromTranslationJsonArray(translationsJsonArr);
            String targetLanguage = GetLanguageNameByLanguageCode(targetLanguageCode);

            return new TranslationResponse(originalTextArr, translatedTextArr, sourceLanguage, targetLanguage);
        } catch (Exception e) {
            throw new TranslationNotSuccessfulException();
        }
    }

    public static boolean IsSupportedLanguageCode(String languageCode) {
        return LANGUAGES.containsKey(languageCode);
    }

    public static String GetLanguageNameByLanguageCode(String languageCode) {
        if (LANGUAGES.containsKey(languageCode)) {
            return LANGUAGES.get(languageCode);
        }

        return null;
    }

    public static String GetLanguagesListFormatted() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : LANGUAGES.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(key + ": " + value + "\n");
        }

        return sb.toString();
    }

}
