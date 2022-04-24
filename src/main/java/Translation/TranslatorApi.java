package Translation;

import org.jsoup.nodes.Document;

public interface TranslatorApi {
    Document GetTranslatedDocument(String[] originalTextArr, String targetLanguageCode);

    Document GetTargetLanguagesListDocument(boolean isTarget);
}
