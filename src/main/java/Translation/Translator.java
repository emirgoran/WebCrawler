package Translation;

import Data.Translation;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;

// TODO: Remove this interface. Its existence is not justified (as it has only one implementor, and will probably never need another one).
public interface Translator {
    Translation TranslateText(String[] textsArray, String targetLanguageCode) throws TranslationNotSuccessfulException, TranslationInvalidArgumentException;

    String GetTargetLanguagesListFormatted();

    boolean IsSupportedTargetLanguageCode(String languageCode);
}
