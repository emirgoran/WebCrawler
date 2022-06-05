package Crawlers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class LinksCrawler {

    public static final int MAX_LINKED_WEBSITES = 3;

    public static List<String> GetUrlsFromDocument(Document document) {
        List<String> urls = new ArrayList<>();
        int pagesCount = 0;

        // Links in HTML are the elements with tag "a" having "href" attribute.
        Elements links = document.select("a[href]");

        for (Element link : links) {
            // Get absolute links for completeness reasons.
            urls.add(link.absUrl("href"));

            if (++pagesCount >= MAX_LINKED_WEBSITES) {
                break;
            }
        }

        return urls;
    }
}
