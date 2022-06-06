package Crawlers;

import Data.WebDocument;

import java.util.List;

public class LinksCrawler {

    public static final int MAX_LINKED_WEBSITES = 3;

    public static List<String> GetUrlsFromDocument(WebDocument document) {
        List<String> urls = document.getAbsoluteUrlsFromDocument();

        while (urls.size() > MAX_LINKED_WEBSITES) {
            urls.remove(0);
        }

        return urls;
    }
}
