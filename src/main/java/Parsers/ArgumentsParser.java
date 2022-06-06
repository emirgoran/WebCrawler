package Parsers;

import Translation.Translator;
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
        Document parsedDocument = null;
        try {
            parsedDocument = documentParser.ParseUrl(URL);
        } catch (Exception e) {
            return false;
        }

        if (parsedDocument == null) {
            return false;
        }

        return true;
    }

    public static boolean ValidateUrls(DocumentParser documentParser, String[] URLs) {
        if (URLs == null || URLs.length == 0) {
            return false;
        }

        boolean result = true;

        for (String url : URLs) {
            if (!ValidateUrl(documentParser, url)) {
                System.err.println("Invalid URL: " + url);
                result = false;
            }
        }

        return result;
    }
}
