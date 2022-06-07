package Translation;

import Data.Translation;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;

import java.util.Map;

public class TranslatorService implements Translator {

    TranslatorApi translatorApi;

    public TranslatorService(TranslatorApi translatorApi) {

        this.translatorApi = translatorApi;
    }

    public Translation TranslateText(String originalTextArr[], String targetLanguageCode)
            throws TranslationNotSuccessfulException, TranslationInvalidArgumentException {
        if (originalTextArr == null || targetLanguageCode == null || !IsSupportedTargetLanguageCode(targetLanguageCode)) {
            throw new TranslationInvalidArgumentException();
        }

        try {
            return translatorApi.GetTranslation(originalTextArr, targetLanguageCode);
        }
        catch (Exception e) {
            throw new TranslationNotSuccessfulException();
        }
    }

    public boolean IsSupportedTargetLanguageCode(String languageCode) {
        return translatorApi.GetAvailableTargetLanguagesHashMap().containsKey(languageCode);
    }

    public String GetTargetLanguagesListFormatted() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : translatorApi.GetAvailableTargetLanguagesHashMap().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(key + ": " + value + "\n");
        }

        return sb.toString();
    }
}
