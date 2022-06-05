import Data.Heading;
import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Markdown.MarkdownWebsiteSummary;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import Parsers.JsoupDocumentParser;
import Translation.Translator;
import Translation.Translation;
import Translation.JsoupTranslatorApi;
import Translation.TranslatorService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static Crawlers.WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages;

public class Main {
    public final static String DEFAULT_SUMMARY_FILE_PATH = new File("summary.md").getAbsolutePath();
    public static final int MAX_URL_DEPTH = 1;

    public static void main(String[] args) throws IOException {

        Translator translator = new TranslatorService(new JsoupTranslatorApi());
        DocumentParser documentParser = new JsoupDocumentParser();

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

    public static void TranslateDocumentAndWriteToFile(DocumentParser documentParser, String[] urls, Integer maxHeadingsDepth, Translator translator, String targetLanguageCode)
            throws IOException, TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);

        List<Website> websitesList = new ArrayList<>();

        for (int i = 0; i < urls.length; i++) {
            Website website = new Website(urls[i], Website.WebsiteStatus.NOT_CRAWLED, maxHeadingsDepth);
            website.setMaxUrlDepth(MAX_URL_DEPTH);
            websitesList.add(website);
        }

        CrawlAndTranslateWebsitesRecursively(websitesList, documentParser, translator, targetLanguageCode, maxHeadingsDepth, MAX_URL_DEPTH);

        for (Website website : websitesList) {
            StringBuilder markdownStringBuilder = MarkdownWebsiteSummary.CreateSummaryForWebsite(website, website.getLinkedTranslation().getSourceLanguage(), website.getLinkedTranslation().getTargetLanguage());
            summaryFileWriter.write(markdownStringBuilder.toString());
        }

        summaryFileWriter.close();
    }

    public static void CrawlAndTranslateWebsitesRecursively(List<Website> websitesList, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepth)
            throws TranslationInvalidArgumentException, TranslationNotSuccessfulException, IOException {

        if (untilDepth-- <= 0) {
            return;
        }

        // TODO: Must parallelize this one !
        for (Website website : websitesList) {
            CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, maxHeadingsDepth); // TODO: parallelize!
            TranslateWebsiteHeadings(website, translator, targetLanguageCode);

            // Let the website know its depth relative to the root parent (optional, future proofing).
            website.setMaxUrlDepth(untilDepth + 1);

            // Recursive call here !
            CrawlAndTranslateWebsitesRecursively(website.getLinkedWebsitesList(), documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepth);
        }
    }

    /**
     * Translate website headings and return the Translation object.
     */
    public static void TranslateWebsiteHeadings(Website website, Translator translator, String targetLanguageCode)
            throws TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        List<String> headingsStringList = new LinkedList<>();

        for (Heading heading : website.getHeadingsList()) {
            headingsStringList.add(heading.getText());
        }

        Translation translation = translator.TranslateText(headingsStringList.toArray(new String[0]), targetLanguageCode);

        for (int i = 0; i < translation.getTranslatedText().length; i++) {
            website.getHeadingsList().get(i).setText(translation.getTranslatedText()[i]);
        }

        website.setLinkedTranslation(translation);
    }
}
