package Crawlers;

import Data.JsoupWebDocument;
import Data.WebDocument;
import Data.Website;
import Parsers.DocumentParser;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebsiteCrawlerTest {

    DocumentParser documentParser;

    @BeforeEach
    void SetUp() throws IOException {
        WebDocument webDocument = new JsoupWebDocument(getJsoupDocument());

        documentParser = mock(DocumentParser.class);

        when(documentParser.ParseUrl(any()))
                .thenReturn(webDocument);
    }

    @Test
    void crawlWebsiteHeadingsAndLinkedPages_assertSame_ok() throws IOException {
        Website websiteIn = new Website("URL", Website.WebsiteStatus.NOT_CRAWLED, 6);
        Website websiteOut = WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages(documentParser, websiteIn, 6);

        assertSame(websiteIn, websiteOut);
    }

    @Test
    void crawlWebsiteHeadingsAndLinkedPages_documentParserReturnsNull_cannotCrawl() throws IOException {
        when(documentParser.ParseUrl(any())).thenReturn(null);

        Website website = new Website("URL", Website.WebsiteStatus.NOT_CRAWLED, 6);
        WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, 6);

        assertEquals(Website.WebsiteStatus.BROKEN, website.getStatus());
    }

    @Test
    void crawlWebsiteHeadingsAndLinkedPages_testHeadings_returnsAll() throws IOException {
        Website website = new Website("URL", Website.WebsiteStatus.NOT_CRAWLED, 6);
        WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, 6);

        assertEquals(6, website.getHeadingsList().size());

        for (int i = 1; i < 7; i++) {
            assertEquals("Heading " + i, website.getHeadingsList().get(i-1).getText());
        }
    }

    @Test
    void crawlWebsiteHeadingsAndLinkedPages_testLinkedPages_returnsAll() throws IOException {
        Website website = new Website("URL", Website.WebsiteStatus.NOT_CRAWLED, 6);
        WebsiteCrawler.CrawlWebsiteHeadingsAndLinkedPages(documentParser, website, 6);

        assertEquals(3, website.getLinkedWebsitesList().size());
        assertEquals("https://www.github.com", website.getLinkedWebsitesList().get(0).getURL());
        assertEquals("https://www.twitter.com", website.getLinkedWebsitesList().get(1).getURL());
        assertEquals("", website.getLinkedWebsitesList().get(2).getURL());
    }

    private Document getJsoupDocument() {
        Document document = new Document("");
        document.append("<h1> Heading 1 </h1>");
        document.append("<h2> Heading 2 </h2>");
        document.append("<h3> Heading 3 </h3>");
        document.append("<h4> Heading 4 </h4>");
        document.append("<h5> Heading 5 </h5>");
        document.append("<h6> Heading 6 </h6>");

        document.append("<a href=\"https://www.github.com\"> GitHub </a>");
        document.append("<a href=\"https://www.twitter.com\"> Twitter </a>");
        document.append("<a href=\"www.wikipedia.org\"> Wikipedia </a>"); // Without an absolute URL

        return document;
    }
}