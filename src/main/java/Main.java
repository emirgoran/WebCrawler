import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {

    public static final int MAX_URL_DEPTH = 3;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Invalid arguments.\nCorrect format <URL> <DEPTH> <TARGET LANGUAGE>" +
                    "where DEPTH is an integer greater than 0, and TARGET LANGUAGE is one of the following:\n" +
                    ArgumentsParser.GetSupportedLanguagesString());
        }

        String URL = args[0], TARGET_LANGUAGE = args[2];
        Integer DEPTH = ArgumentsParser.ParseDepth(args[1]);

        if (!ValidateDepth(DEPTH) || !ValidateLanguage(TARGET_LANGUAGE) || !ValidateUrl(URL)) {
            return;
        }

        MarkdownWebsiteSummary.CreateSummaryForWebsite(URL, DEPTH, MAX_URL_DEPTH, TARGET_LANGUAGE);
    }

    public static boolean ValidateDepth(Integer depth) {
        if (depth == null || depth < 1)
        {
            System.err.println("Unsupported depth! Depth should be a positive, non-zero integer.");
            return false;
        }

        return true;
    }

    public static boolean ValidateLanguage(String language) {
        if (!ArgumentsParser.IsSupportedLanguage(language))
        {
            System.err.println("Unsupported target language: \"" + language + "\"\n" +
                    "Supported target languages: " + ArgumentsParser.GetSupportedLanguagesString() + ".");
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
