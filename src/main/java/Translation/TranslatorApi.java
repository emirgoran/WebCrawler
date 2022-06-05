package Translation;

import org.json.JSONArray;

public interface TranslatorApi {
    JSONArray GetTranslationsJsonArray(String[] originalTextArr, String targetLanguageCode);

    JSONArray GetLanguagesJsonArray(boolean isTarget);
}
