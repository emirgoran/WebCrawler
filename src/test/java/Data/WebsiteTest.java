package Data;

import Crawlers.WebsiteCrawler;
import Parsers.DocumentParser;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebsiteTest {

    @Test
    void crawlWebsite_crawlEmptyPage_returnsEmpty() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(anyString())).thenReturn(new Document(""));

        Website website = new Website("URL", 6, 2);
        WebsiteCrawler.CrawlWebsite(documentParser, website);

        assertEquals("URL", website.getURL());
        assertEquals(6, website.getMaxHeadingsDepth());
        assertEquals(2, website.getMaxUrlDepth());
        assertEquals(0, website.getHeadingsList().size());
        assertEquals(0, website.getLinkedWebsitesList().size());
        assertEquals(Website.WebsiteStatus.OK, website.getStatus());
    }

    @Test
    void crawlWebsite_crawlSimplePage_returnsOk() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(anyString())).thenReturn(GetSimpleDocument());

        Website website = new Website("URL", 6, 2);
        WebsiteCrawler.CrawlWebsite(documentParser, website);

        assertEquals("URL", website.getURL());
        assertEquals(6, website.getMaxHeadingsDepth());
        assertEquals(2, website.getMaxUrlDepth());
        assertEquals(6, website.getHeadingsList().size());
        assertEquals(1, website.getLinkedWebsitesList().size());
        assertEquals(Website.WebsiteStatus.OK, website.getStatus());
    }

    @Test
    void crawlWebsite_crawlSimplePageAndTestInner_returnsOk() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(anyString())).thenReturn(GetSimpleDocument());
        Website websiteOuter = new Website("URL", 6, 2);
        WebsiteCrawler.CrawlWebsite(documentParser, websiteOuter);

        Website website = websiteOuter.getLinkedWebsitesList().get(0);

        assertEquals("http:\\\\www.test-website1.com", website.getURL());
        assertEquals(6, website.getMaxHeadingsDepth());
        assertEquals(1, website.getMaxUrlDepth());
        assertEquals(6, website.getHeadingsList().size());
        assertEquals(1, website.getLinkedWebsitesList().size());
        assertEquals(Website.WebsiteStatus.OK, website.getStatus());
    }

    @Test
    void crawlWebsite_crawlSimplePageAndTestInnerInner_returnsNotCrawled() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(anyString())).thenReturn(GetSimpleDocument());
        Website websiteOuter = new Website("URL", 6, 2);
        WebsiteCrawler.CrawlWebsite(documentParser, websiteOuter);

        Website website = websiteOuter.getLinkedWebsitesList().get(0).getLinkedWebsitesList().get(0);

        assertEquals("http:\\\\www.test-website1.com", website.getURL());
        assertEquals(6, website.getMaxHeadingsDepth());
        assertEquals(0, website.getMaxUrlDepth());
        assertEquals(0, website.getHeadingsList().size());
        assertEquals(0, website.getLinkedWebsitesList().size());
        assertEquals(Website.WebsiteStatus.NOT_CRAWLED, website.getStatus());
    }

    private Document GetSimpleDocument() {
        Document document = new Document("");

        document.append("<h0> Heading 0 </h0>"); // invalid one
        document.append("<h1> Heading 1 </h1>");
        document.append("<h2> Heading 2 </h2>");
        document.append("<h3> Heading 3 </h3>");
        document.append("<h4> Heading 4 </h4>");
        document.append("<h5> Heading 5 </h5>");
        document.append("<h6> Heading 6 </h6>");
        document.append("<h7> Heading 7 </h7>"); // invalid one

        document.append("<a href=\"http:\\\\www.test-website1.com\"> Test website 1 </a>");

        return document;
    }
}