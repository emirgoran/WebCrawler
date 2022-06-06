import Data.Heading;
import Data.Website;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import Parsers.DocumentParser;
import Translation.Translation;
import Translation.Translator;

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
    public Website call() throws Exception {
        // Crawl website headings and linked pages.
        CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, maxHeadingsDepth);

        // Let the website know its (inverse) depth relative to the root parent (optional, future proofing).
        website.setMaxUrlDepth(untilDepth);

        // ExecutorService for two tasks we need to parallelize: Crawling, Translating.
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ArrayList<Callable<Void>> tasks = new ArrayList<>(2);
        tasks.add(GetCrawlAndTranslateWebsitesRecursivelyCallableTask(website.getLinkedWebsitesList(), documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepth));
        tasks.add(GetTranslateWebsiteHeadingsCallableTask(website, translator, targetLanguageCode));

        List<Future<Void>> websitesFutureList = executorService.invokeAll(tasks);

        // Join all tasks' threads and throw (propagate) ExecutionException if the underlying task threw an exception.
        // The underlying exception/cause will be available through Throwable.getCause() method.
        for (Future<Void> websiteFuture : websitesFutureList) {
            websiteFuture.get();
        }

        executorService.shutdown();
        return website;
    }

    // Even though it uses multithreading internally, this function is a blocking one.
    public static void CrawlAndTranslateWebsitesRecursively(List<Website> websitesList, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepthNew)
            throws InterruptedException, ExecutionException {

        // Attention: Recursion stop condition!
        if (untilDepthNew - 1 <= 0) {
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(websitesList.size());
        ArrayList<Callable<Website>> tasks = new ArrayList<>(websitesList.size());

        // TODO: Don't forget to catch exceptions and route them back to the caller somehow!
        for (Website website : websitesList) {
            tasks.add(new CallableCrawlAndTranslateWebsiteTask(website, documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepthNew - 1));
        }

        List<Future<Website>> websitesFutureList = executorService.invokeAll(tasks);

        // Join all tasks' threads and throw (propagate) ExecutionException if the underlying task threw an exception.
        // The underlying exception/cause will be available through Throwable.getCause() method.
        for (Future<Website> websiteFuture : websitesFutureList) {
            websiteFuture.get();
        }

        executorService.shutdown();
    }

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

    private static Callable<Void> GetCrawlAndTranslateWebsitesRecursivelyCallableTask(List<Website> websitesList, DocumentParser documentParser, Translator translator, String targetLanguageCode, int maxHeadingsDepth, int untilDepthNew) {
        return () -> {
            CrawlAndTranslateWebsitesRecursively(websitesList, documentParser, translator, targetLanguageCode, maxHeadingsDepth, untilDepthNew);
            return null;
        };
    }

    private static Callable<Void> GetTranslateWebsiteHeadingsCallableTask(Website website, Translator translator, String targetLanguageCode) {
        return () -> {
            TranslateWebsiteHeadings(website, translator, targetLanguageCode);
            return null;
        };
    }
}
