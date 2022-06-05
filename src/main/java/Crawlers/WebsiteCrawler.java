package Crawlers;

import Data.Heading;
import Data.Website;
import Parsers.DocumentParser;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebsiteCrawler {

    public static final int MAX_INNER_WEBSITES_NUM = 5;

    public static void CrawlWebsite(DocumentParser documentParser, Website website) {
        Document webDocument = null;

        try {
            webDocument = documentParser.ParseUrl(website.getURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (webDocument == null) {
            website.setStatus(Website.WebsiteStatus.BROKEN);
            return;
        }

        if (website.getMaxUrlDepth() < 1) {
            website.setStatus(Website.WebsiteStatus.NOT_CRAWLED);
            return;
        }

        website.setHeadingsList(GetHeadingsFromDocument(webDocument, website.getMaxHeadingsDepth()));

        CrawlLinkedWebsites(documentParser, webDocument, website);
        website.setStatus(Website.WebsiteStatus.OK);
    }

    /* Get headings from the web document. */
    private static List<Heading> GetHeadingsFromDocument(Document document, int maxHeadingsDepth) {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(maxHeadingsDepth);
        return HeadingsCrawler.GetHeadingsFromDocument(document, headingLevel);
    }

    /* Find URLs on a webpage, then recursively crawl those URLs until the maxUrlDepth is reached. */
    /* Also limit the number of linked websites due to the number of websites getting out of control. */
    private static void CrawlLinkedWebsites(DocumentParser documentParser, Document document, Website website) {
        List<String> linkedUrls = LinksCrawler.GetUrlsFromDocument(document);

        List<Website> linkedWebsites = CrawlWebsites(documentParser, linkedUrls, website.getMaxHeadingsDepth(), website.getMaxUrlDepth() - 1);

        if (linkedWebsites.size() > MAX_INNER_WEBSITES_NUM) {
            website.setLinkedWebsitesList(linkedWebsites.subList(0 , MAX_INNER_WEBSITES_NUM - 1));
        } else {
            website.setLinkedWebsitesList(linkedWebsites);
        }
    }

    /* Recursively crawl websites until the maxUrlDepth is reached. */
    public static List<Website> CrawlWebsites(DocumentParser documentParser, List<String> URLs, int maxHeadingsDepth, int maxUrlDepth) {
        List<Website> websiteList = new ArrayList<>();

        // TODO: A place to add multi-threading!
        for (String url : URLs) {
            Website website = new Website(url, maxHeadingsDepth, maxUrlDepth);
            CrawlWebsite(documentParser, website);
            websiteList.add(website);
        }

        return websiteList;
    }

    public static Website CrawlWebsiteHeadingsAndLinkedPages(DocumentParser documentParser, Website website, int maxHeadingsDepth) throws IOException {
        Document webDocument = documentParser.ParseUrl(website.getURL());

        website.setStatus(Website.WebsiteStatus.OK);

        if (webDocument == null) {
            website.setStatus(Website.WebsiteStatus.BROKEN);
        }

        website.setHeadingsList(GetHeadingsFromDocument(webDocument, maxHeadingsDepth));

        for (String innerUrl : LinksCrawler.GetUrlsFromDocument(webDocument)) {
            website.getLinkedWebsitesList().add(new Website(innerUrl, Website.WebsiteStatus.NOT_CRAWLED, maxHeadingsDepth));
        }

        return website;
    }
}
