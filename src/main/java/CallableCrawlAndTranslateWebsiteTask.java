import Data.Heading;
import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Parsers.DocumentParser;
import Translation.Translation;
import Translation.Translator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static Crawlers.WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages;

public class CallableCrawlAndTranslateWebsiteTask implements Callable<Website> {

    private Website website;
    private DocumentParser documentParser;
    private Translator translator;
    private String targetLanguageCode;
    private int maxHeadingsDepth;
    private int untilDepth;

    public CallableCrawlAndTranslateWebsiteTask(Website website, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepth) {
        this.website = website;
        this.documentParser = documentParser;
        this.translator = translator;
        this.targetLanguageCode = targetLanguageCode;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.untilDepth = untilDepth;
    }

    @Override
    public Website call() throws IOException, InterruptedException, ExecutionException {
        // Crawl website headings and linked pages.
        CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, maxHeadingsDepth);

        // ExecutorService for two tasks we need to parallelize: Crawling, Translating.
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ArrayList<Callable<Website>> tasks = new ArrayList<>(2);
        tasks.add(GetCrawlAndTranslateWebsitesRecursivelyCallableTask(website, documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepth));
        tasks.add(GetTranslateWebsiteHeadingsCallableTask(website, translator, targetLanguageCode));
        List<Future<Website>> websitesFutureList = executorService.invokeAll(tasks);

        // Join all tasks' threads and throw (propagate) ExecutionException if the underlying task threw an exception.
        // The underlying exception/cause will be available through Throwable.getCause() method.
        try {
            for (Future<Website> websiteFuture : websitesFutureList) {
                websiteFuture.get();
            }
        } catch (Exception e) {
            // Simply propagate the exception back to parent.
            throw e;
        } finally {
            // Always make sure threads are shutdown; otherwise, it will hang the whole program.
            executorService.shutdown();
        }

        return website;
    }

    // Even though it uses multithreading internally, this function is a blocking one.
    public static void CrawlAndTranslateLinkedWebsitesRecursively(Website website, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepthNew)
            throws InterruptedException, ExecutionException {

        // Attention: Recursion stop condition!
        if (untilDepthNew - 1 <= 0) {
            return;
        }

        ArrayList<Callable<Website>> tasks = new ArrayList<>(website.getLinkedWebsitesList().size());

        // TODO: Don't forget to catch exceptions and route them back to the caller somehow!
        for (Website innerWebsite : website.getLinkedWebsitesList()) {
            tasks.add(new CallableCrawlAndTranslateWebsiteTask(innerWebsite, documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepthNew - 1));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(website.getLinkedWebsitesList().size());
        List<Future<Website>> websitesFutureList = executorService.invokeAll(tasks);

        // Join all tasks' threads and throw (propagate) ExecutionException if the underlying task threw an exception.
        // The underlying exception/cause will be available through Throwable.getCause() method.
        try {
            for (Future<Website> websiteFuture : websitesFutureList) {
                websiteFuture.get();
            }
        } catch (Exception e) {
            // Simply propagate the exception back to parent.
            throw e;
        } finally {
            // Always make sure threads are shutdown.
            executorService.shutdown();
        }
    }

    private static void TranslateWebsiteHeadings(Website website, Translator translator, String targetLanguageCode)
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

    private static Callable<Website> GetCrawlAndTranslateWebsitesRecursivelyCallableTask(Website website, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepthNew) {
        return () -> {
            CrawlAndTranslateLinkedWebsitesRecursively(website, documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepthNew);
            return website;
        };
    }

    private static Callable<Website> GetTranslateWebsiteHeadingsCallableTask(Website website, Translator translator, String targetLanguageCode) {
        return () -> {
            TranslateWebsiteHeadings(website, translator, targetLanguageCode);
            return website;
        };
    }
}
