import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Translation.TranslatorService;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {

    public static final int MAX_URL_DEPTH = 2;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Invalid arguments.\nCorrect format <URL> <DEPTH> <TARGET LANGUAGE_CODE>" +
                    "where DEPTH is an integer greater than 0, and TARGET LANGUAGE CODE is one of the following:\n" +
                    TranslatorService.GetTargetLanguagesListFormatted());

            return;
        }

        String URL = args[0], TARGET_LANGUAGE = args[2];
        Integer DEPTH = ArgumentsParser.ParseDepth(args[1]);

        if (!ValidateDepth(DEPTH) || !ValidateLanguage(TARGET_LANGUAGE) || !ValidateUrl(URL)) {
            return;
        }

        try {
            MarkdownWebsiteSummary.CreateSummaryForWebsite(URL, DEPTH, MAX_URL_DEPTH, TARGET_LANGUAGE);
        } catch (TranslationInvalidArgumentException e) {
            System.err.println("Could not find any text to translate!");
        } catch (TranslationNotSuccessfulException e) {
            System.err.println("An error occurred during translation procedure!");
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
