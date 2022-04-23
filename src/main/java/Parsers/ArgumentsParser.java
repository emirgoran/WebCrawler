package Parsers;

import Translation.TranslatorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArgumentsParser {

    public static boolean IsSupportedLanguageCode(String language)
    {
        if (language == null || language.length() == 0) {
            return false;
        }

        return TranslatorService.IsSupportedTargetLanguageCode(language);
    }

    public static Integer ParseDepth(String depthString)
    {
        try {
            return Integer.parseInt(depthString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Document ParseUrl(String URL)
    {
        try {
            return Jsoup.connect(URL).get();
        } catch (Exception e) {
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

    public static boolean ValidateLanguage(String languageCode) {
        if (!ArgumentsParser.IsSupportedLanguageCode(languageCode))
        {
            System.err.println("Unsupported target language: \"" + languageCode + "\"\n" +
                    "Supported target languages:\n" + TranslatorService.GetTargetLanguagesListFormatted());
            return false;
        }

        return true;
    }

    public static boolean ValidateUrl(String URL) {
        Document parsedDocument = ArgumentsParser.ParseUrl(URL);
        if (parsedDocument == null) {
            System.err.println("The entered URL might be invalid or there is no connection to the server!");
            return false;
        }

        return true;
    }
}
