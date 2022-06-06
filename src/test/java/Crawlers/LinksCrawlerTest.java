package Crawlers;

import Data.JsoupWebDocument;
import Data.WebDocument;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class LinksCrawlerTest {

    @Test
    void getUrlsFromWebsite_noUrlsOnWebsite_returnsEmpty() {
        WebDocument document = new JsoupWebDocument(new Document("localhost"));

        List<String> urls = LinksCrawler.GetUrlsFromWebDocument(document);

        Assertions.assertEquals(0, urls.size());
    }

    @Test
    void getUrlsFromWebsite_testNormalCase_returnsListOfUrls() {
        WebDocument document = new JsoupWebDocument(new Document("localhost"));
        document.append("<a href=\"http:\\\\www.test-website.com\"> Test website </a>");

        List<String> urls = LinksCrawler.GetUrlsFromWebDocument(document);

        Assertions.assertEquals(1, urls.size());
        Assertions.assertEquals("http:\\\\www.test-website.com", urls.get(0));
    }

    @Test
    void getUrlsFromWebsite_testMaxNumberOfLinksExactly_returnsTheListOfUrls() {
        WebDocument document = new JsoupWebDocument(new Document("localhost"));
        for (int i = 0; i < LinksCrawler.MAX_LINKED_WEBSITES; i++) {
            document.append("<a href=\"http:\\\\www.test-website.com\"> Test website </a>");
        }

        List<String> urls = LinksCrawler.GetUrlsFromWebDocument(document);

        Assertions.assertEquals(LinksCrawler.MAX_LINKED_WEBSITES, urls.size());
        for (int i = 0; i < LinksCrawler.MAX_LINKED_WEBSITES; i++) {
            Assertions.assertEquals("http:\\\\www.test-website.com", urls.get(i));
        }
    }

    @Test
    void getUrlsFromWebsite_testMaxNumberOfLinksMoreThanAllowed_returnsReducedListOfUrls() {
        WebDocument document = new JsoupWebDocument(new Document("localhost"));
        for (int i = 0; i < LinksCrawler.MAX_LINKED_WEBSITES + 1; i++) {
            document.append("<a href=\"http:\\\\www.test-website.com\"> Test website </a>");
        }

        List<String> urls = LinksCrawler.GetUrlsFromWebDocument(document);

        Assertions.assertEquals(LinksCrawler.MAX_LINKED_WEBSITES, urls.size());
        for (int i = 0; i < LinksCrawler.MAX_LINKED_WEBSITES; i++) {
            Assertions.assertEquals("http:\\\\www.test-website.com", urls.get(i));
        }
    }
}