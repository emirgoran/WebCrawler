import Crawlers.WebsiteCrawler;
import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Markdown.MarkdownWebsiteSummary;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import Parsers.WebsiteDocumentParser;
import Translation.Translator;
import Translation.Translation;
import Translation.TranslatorApiImpl;
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

        Translator translator = new TranslatorService(new TranslatorApiImpl());
        DocumentParser documentParser = new WebsiteDocumentParser();

        if (args.length < 3) {
            System.err.println("Invalid arguments.\nCorrect format <HEADINGS_DEPTH> <TARGET_LANGUAGE_CODE> <URL ...>" +
                    "where DEPTH is an integer greater than 0, and TARGET LANGUAGE CODE is one of the following:\n" +
                    translator.GetTargetLanguagesListFormatted());

            return;
        }

        Integer maxHeadingDepth = ArgumentsParser.ParseDepth(args[0]);
        String targetLanguage = args[1];
        String[] urls = Arrays.copyOfRange(args, 2, args.length);

        if (!ArgumentsParser.ValidateDepth(maxHeadingDepth) || !ArgumentsParser.ValidateLanguage(translator, targetLanguage)|| !ArgumentsParser.ValidateUrls(documentParser, urls)) {
            return;
        }

        try {
            TranslateDocumentAndWriteToFile(documentParser, urls, maxHeadingDepth, translator, targetLanguage);
        } catch (TranslationInvalidArgumentException e) {
            System.err.println("Could not find any text to translate!");
        } catch (TranslationNotSuccessfulException e) {
            System.err.println("An error occurred during translation procedure!");
        }

        System.out.println("Output file: " + DEFAULT_SUMMARY_FILE_PATH);
    }

    public static void TranslateDocumentAndWriteToFile(DocumentParser documentParser, String[] urls, Integer maxHeadingDepth, Translator translator, String targetLanguage)
            throws IOException, TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);

        // TODO: A place to add multi-threading!
        for (String url : urls) {
            Website website = new Website(url, maxHeadingDepth, MAX_URL_DEPTH);

            WebsiteCrawler.CrawlWebsite(documentParser, website);

            Translation translation = TranslateWebsitesHeadingsRecursively(website, translator, targetLanguage);

            StringBuilder markdownStringBuilder = MarkdownWebsiteSummary.CreateSummaryForWebsite(website, translation.getSourceLanguage(), translation.getTargetLanguage());

            summaryFileWriter.write(markdownStringBuilder.toString());
        }

        summaryFileWriter.close();
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
