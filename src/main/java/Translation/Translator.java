package Translation;

import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;

public interface Translator {
    Translation TranslateText(String[] textsArray, String targetLanguageCode) throws TranslationNotSuccessfulException, TranslationInvalidArgumentException;

    String GetTargetLanguagesListFormatted();

    boolean IsSupportedTargetLanguageCode(String languageCode);
}
