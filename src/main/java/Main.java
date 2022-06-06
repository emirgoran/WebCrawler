import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Markdown.MarkdownWebsiteSummary;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import Parsers.JsoupDocumentParser;
import Translation.Translator;
import Translation.JsoupTranslatorApi;
import Translation.TranslatorService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public final static String DEFAULT_SUMMARY_FILE_PATH = new File("summary.md").getAbsolutePath();
    public static final int MAX_URL_DEPTH = 2;

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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Output file: " + DEFAULT_SUMMARY_FILE_PATH);
    }

    public static void TranslateDocumentAndWriteToFile(DocumentParser documentParser, String[] urls, Integer maxHeadingsDepth, Translator translator, String targetLanguageCode)
            throws IOException, TranslationInvalidArgumentException, TranslationNotSuccessfulException, InterruptedException, ExecutionException
    {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);
        ExecutorService executorService = Executors.newFixedThreadPool(urls.length);
        ArrayList<Callable<Website>> tasks = new ArrayList<>(urls.length);

        // Initialize empty websites and create crawling&translating tasks for them.
        for (int i = 0; i < urls.length; i++) {
            Website website = new Website(urls[i], Website.WebsiteStatus.NOT_CRAWLED, maxHeadingsDepth);
            tasks.add(new CallableCrawlAndTranslateWebsiteTask(website, documentParser, translator, targetLanguageCode, maxHeadingsDepth, MAX_URL_DEPTH));
        }

        // Invoke all website crawling&translating tasks.
        List<Future<Website>> websitesFutureList = executorService.invokeAll(tasks);

        // Acts like the join() method from Thread.
        for (Future<Website> websiteFuture : websitesFutureList) {
            try {
                // This call will block until the underlying task is finished, and will throw an exception if the underlying task threw an exception.
                Website website = websiteFuture.get();

                // Create a summary and send it to the writer.
                StringBuilder markdownStringBuilder = MarkdownWebsiteSummary.CreateSummaryForWebsite(website, website.getLinkedTranslation().getSourceLanguage(), website.getLinkedTranslation().getTargetLanguage());
                summaryFileWriter.write(markdownStringBuilder.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                // TODO: Try to get the underlying cause/exception!
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();
        summaryFileWriter.close();
    }
}
