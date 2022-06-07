package Crawlers;

import Data.WebDocument;

import java.util.List;

public class LinksCrawler {

    // Attention: Increasing this constant's value will significantly increase runtime!
    public static final int MAX_LINKED_WEBSITES = 3;

    public static List<String> GetUrlsFromWebDocument(WebDocument webDocument) {
        List<String> urls = webDocument.getAbsoluteUrlsFromWebDocument();

        while (urls.size() > MAX_LINKED_WEBSITES) {
            urls.remove(0);
        }

        return urls;
    }
}
