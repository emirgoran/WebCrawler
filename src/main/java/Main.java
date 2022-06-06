import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Markdown.MarkdownWebsiteSummary;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import Parsers.JsoupDocumentParser;
import Translation.JsoupTranslatorApi;
import Translation.Translator;
import Translation.TranslatorService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private final static String DEFAULT_SUMMARY_FILE_PATH = new File("summary.md").getAbsolutePath();
    private static final int MAX_URL_DEPTH = 1;

    public static void main(String[] args) {

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
            System.out.println("Output file: " + DEFAULT_SUMMARY_FILE_PATH);
        } catch (IOException e) {
            System.err.println("IOException: Could not create, append or save the summary file!");
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: One or more of the underlying crawling or translating unfinished tasks have been forcibly cancelled.");
        }
    }

    private static void TranslateDocumentAndWriteToFile(DocumentParser documentParser, String[] urls, Integer maxHeadingsDepth, Translator translator, String targetLanguageCode)
            throws IOException, InterruptedException {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);
        ArrayList<Website> websiteArrayList = new ArrayList<>(urls.length);
        ArrayList<Callable<Website>> tasks = new ArrayList<>(urls.length);

        // Initialize empty websites and create crawling and translating tasks for them.
        for (int i = 0; i < urls.length; i++) {
            Website website = new Website(urls[i], Website.WebsiteStatus.NOT_CRAWLED, maxHeadingsDepth);
            websiteArrayList.add(website);
            tasks.add(new CallableCrawlAndTranslateWebsiteTask(website, documentParser, translator, targetLanguageCode, maxHeadingsDepth, MAX_URL_DEPTH));
        }

        // Invoke all website crawling and translating tasks.
        ExecutorService executorService = Executors.newFixedThreadPool(urls.length);
        List<Future<Website>> websitesFutureList = executorService.invokeAll(tasks);

        // Acts like the join() method from Thread (initial order is preserved).
        for (Future<Website> websiteFuture : websitesFutureList) {
            Website website = websiteArrayList.get(websitesFutureList.indexOf(websiteFuture));

            try {
                // This call will block until the underlying task is finished.
                // It will throw an ExecutionException if the underlying task threw any exception.
                websiteFuture.get();

                // Create a summary and send it to the writer.
                StringBuilder markdownStringBuilder = MarkdownWebsiteSummary.CreateSummaryForWebsite(website, website.getLinkedTranslation().getSourceLanguage(), website.getLinkedTranslation().getTargetLanguage());
                summaryFileWriter.write(markdownStringBuilder.toString());
            } catch (ExecutionException e) {
                PrintFormattedInfoAboutExecutionExceptionCause(e, website);
            } finally {
                executorService.shutdownNow();
            }

        }

        summaryFileWriter.close();
    }

    private static void PrintFormattedInfoAboutExecutionExceptionCause(ExecutionException executionException, Website crawledWebsite) {
        // Pay attention to the double getCause() call.
        Throwable cause = executionException.getCause().getCause();

        if (cause instanceof IOException) {
            System.err.println("IOException occurred while trying to fetch website.");
            System.err.println("Some possible causes: no connection to the server, invalid authentication token, access denied.");
        } else if (cause instanceof InterruptedException) {
            System.err.println("InterruptedException occurred while trying to fetch and/or translate website.");
            System.err.println("Possible cause: one or more tasks have been interrupted.");
        } else if (cause instanceof TranslationInvalidArgumentException) {
            System.err.println("TranslationInvalidArgumentException occurred while trying to translate website headings.");
            System.err.println("Possible causes: no source text has been defined, unsupported target language code.");
        } else if (cause instanceof TranslationNotSuccessfulException) {
            System.err.println("TranslationNotSuccessfulException occurred while trying to translate website headings.");
            System.err.println("Some possible causes: no connection to the server, invalid authentication token, access denied.");
        } else {
            System.err.println("Unexpected exception has been thrown while trying to fetch and/or translate website.");
        }

        System.err.println("Affected website: " + crawledWebsite.getURL() + "\n");
    }
}
