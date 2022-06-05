package Crawlers;

import Data.Heading;
import Data.Website;
import Parsers.DocumentParser;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class WebsiteCrawler {

    public static final int MAX_INNER_WEBSITES_NUM = 5;

    public static void CrawlWebsite(DocumentParser documentParser, Website website) {
        Document webDocument = documentParser.ParseUrl(website.getURL());

        if (webDocument == null) {
            website.setStatus(Website.WebsiteStatus.BROKEN);
            return;
        }

        if (website.getMaxUrlDepth() < 1) {
            website.setStatus(Website.WebsiteStatus.NOT_CRAWLED);
            return;
        }

        CrawlHeadings(webDocument, website);
        CrawlLinkedWebsites(documentParser, webDocument, website);
        website.setStatus(Website.WebsiteStatus.OK);
    }

    /* Get headings from the web document. */
    private static void CrawlHeadings(Document document, Website website) {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(website.getMaxHeadingsDepth());
        website.setHeadingsList(HeadingsCrawler.GetHeadingsFromDocument(document, headingLevel));
    }

    /* Find URLs on a webpage, then recursively crawl those URLs until the maxUrlDepth is reached. */
    /* Also limit the number of linked websites due to the number of websites getting out of control. */
    private static void CrawlLinkedWebsites(DocumentParser documentParser, Document document, Website website) {
        List<String> linkedUrls = LinksCrawler.GetUrlsFromWebsite(document);

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
}
