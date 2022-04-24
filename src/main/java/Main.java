import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Markdown.MarkdownWebsiteSummary;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import Parsers.WebsiteDocumentParser;
import Translation.Translator;
import Translation.Translation;
import Translation.TranslatorService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Markdown.MarkdownWebsiteSummary.*;

public class Main {
    public final static String DEFAULT_SUMMARY_FILE_PATH = new File("summary.md").getAbsolutePath();
    public static final int MAX_URL_DEPTH = 2;

    public static void main(String[] args) throws IOException {

        Translator translator = new TranslatorService();
        DocumentParser documentParser = new WebsiteDocumentParser();

        if (args.length != 3) {
            System.err.println("Invalid arguments.\nCorrect format <URL> <DEPTH> <TARGET LANGUAGE_CODE>" +
                    "where DEPTH is an integer greater than 0, and TARGET LANGUAGE CODE is one of the following:\n" +
                    translator.GetTargetLanguagesListFormatted());

            return;
        }

        String url = args[0], targetLanguage = args[2];
        Integer maxHeadingDepth = ArgumentsParser.ParseDepth(args[1]);

        if (!ArgumentsParser.ValidateDepth(maxHeadingDepth) || !ArgumentsParser.ValidateLanguage(translator, targetLanguage)|| !ArgumentsParser.ValidateUrl(documentParser, url)) {
            return;
        }

        try {
            FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);

            Website website = new Website(documentParser, url, maxHeadingDepth, MAX_URL_DEPTH);

            website.CrawlWebsite();

            Translation translation = TranslateWebsitesHeadingsRecursively(website, translator, targetLanguage);

            StringBuilder markdownStringBuilder = MarkdownWebsiteSummary.CreateSummaryForWebsite(website, translation.getSourceLanguage(), translation.getTargetLanguage());

            summaryFileWriter.write(markdownStringBuilder.toString());

            summaryFileWriter.close();
        } catch (TranslationInvalidArgumentException e) {
            System.err.println("Could not find any text to translate!");
        } catch (TranslationNotSuccessfulException e) {
            System.err.println("An error occurred during translation procedure!");
        }

        System.out.println("Output file: " + DEFAULT_SUMMARY_FILE_PATH);
    }

    public static Translation TranslateWebsitesHeadingsRecursively(Website website, Translator translator, String targetLanguageCode)
            throws TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        List<String> collectedHeadings = CollectHeadingsFromWebsitesRecursive(website);

        Translation translation =  translator.TranslateText(collectedHeadings.toArray(new String[0]), targetLanguageCode);

        Queue<String> headingsQueue = new LinkedList(Arrays.asList(translation.getTranslatedText()));
        ApplyHeadingsToWebsitesRecursive(website, headingsQueue);

        return translation;
    }
}
