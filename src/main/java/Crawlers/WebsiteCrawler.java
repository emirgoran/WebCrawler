package Crawlers;

import Data.Heading;
import Data.WebDocument;
import Data.Website;
import Parsers.DocumentParser;

import java.io.IOException;
import java.util.List;

public class WebsiteCrawler {

    /* Get headings from the web document. */
    private static List<Heading> GetHeadingsFromWebDocument(WebDocument webDocument, int maxHeadingsDepth) {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(maxHeadingsDepth);
        return HeadingsCrawler.GetHeadingsFromWebDocument(webDocument, headingLevel);
    }

    public static Website CrawlWebsiteHeadingsAndLinkedPages(DocumentParser documentParser, Website website, int maxHeadingsDepth) throws IOException {
        WebDocument webDocument = documentParser.ParseUrl(website.getURL());

        website.setStatus(Website.WebsiteStatus.OK);

        if (webDocument == null) {
            website.setStatus(Website.WebsiteStatus.BROKEN);
        }

        website.setHeadingsList(GetHeadingsFromWebDocument(webDocument, maxHeadingsDepth));

        for (String innerUrl : LinksCrawler.GetUrlsFromWebDocument(webDocument)) {
            website.getLinkedWebsitesList().add(new Website(innerUrl, Website.WebsiteStatus.NOT_CRAWLED, maxHeadingsDepth));
        }

        return website;
    }
}
