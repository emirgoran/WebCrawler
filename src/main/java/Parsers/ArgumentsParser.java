package Parsers;

import Translation.Translator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArgumentsParser {

    public static boolean IsSupportedLanguageCode(Translator translator, String language)
    {
        if (language == null || language.length() == 0) {
            return false;
        }

        return translator.IsSupportedTargetLanguageCode(language);
    }

    public static Integer ParseDepth(String depthString)
    {
        try {
            return Integer.parseInt(depthString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean ValidateDepth(Integer depth) {
        if (depth == null || depth < 1)
        {
            System.err.println("Unsupported depth! Depth should be a positive, non-zero integer.");
            return false;
        }

        return true;
    }

    public static boolean ValidateLanguage(Translator translator, String languageCode) {
        if (!ArgumentsParser.IsSupportedLanguageCode(translator, languageCode))
        {
            System.err.println("Unsupported target language: \"" + languageCode + "\"\n" +
                    "Supported target languages:\n" + translator.GetTargetLanguagesListFormatted());
            return false;
        }

        return true;
    }

    public static boolean ValidateUrl(DocumentParser documentParser, String URL) {
        Document parsedDocument = documentParser.ParseUrl(URL);
        if (parsedDocument == null) {
            System.err.println("The entered URL might be invalid or there is no connection to the server!");
            return false;
        }

        return true;
    }
}
