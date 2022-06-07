package Translation;

import Data.Translation;

import java.util.HashMap;

public interface TranslatorApi {
    Translation GetTranslation(String[] originalTextArr, String targetLanguageCode);
    HashMap<String, String> GetAvailableTargetLanguagesHashMap();
    HashMap<String, String> GetAvailableSourceLanguagesHashMap();
}
