package Translation;

import org.json.JSONArray;
import org.jsoup.nodes.Document;

public interface TranslatorApi {
    JSONArray GetTranslatedDocument(String[] originalTextArr, String targetLanguageCode);

    JSONArray GetLanguagesListDocument(boolean isTarget);
}
