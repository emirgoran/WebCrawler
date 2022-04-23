import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Translation.TranslatorService;

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

        String url = args[0], targetLanguage = args[2];
        Integer maxHeadingDepth = ArgumentsParser.ParseDepth(args[1]);

        if (!ArgumentsParser.ValidateDepth(maxHeadingDepth) || !ArgumentsParser.ValidateLanguage(targetLanguage)|| !ArgumentsParser.ValidateUrl(url)) {
            return;
        }

        try {
            MarkdownWebsiteSummary.CreateSummaryForWebsite(url, maxHeadingDepth, MAX_URL_DEPTH, targetLanguage);
        } catch (TranslationInvalidArgumentException e) {
            System.err.println("Could not find any text to translate!");
        } catch (TranslationNotSuccessfulException e) {
            System.err.println("An error occurred during translation procedure!");
        }
    }
}
