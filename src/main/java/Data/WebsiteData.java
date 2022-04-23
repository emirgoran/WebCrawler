package Data;

import Crawlers.HeadingsCrawler;
import Crawlers.LinksCrawler;
import Parsers.ArgumentsParser;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class WebsiteData {

    public static final int MAX_INNER_WEBSITES_NUM = 5;

    private String URL;
    private WebsiteStatus status;
    private List<Heading> headingsList;
    private List<WebsiteData> linkedWebsitesList;
    private int maxUrlDepth;
    private int maxHeadingsDepth;

    public WebsiteData(String URL, int maxHeadingsDepth, int maxUrlDepth) {
        this.URL = URL;
        this.status = WebsiteStatus.BROKEN;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.maxUrlDepth = maxUrlDepth;

        CrawlWebsite();
    }

    private void CrawlWebsite() {
        Document webDocument = ArgumentsParser.ParseUrl(this.URL);

        if (webDocument == null) {
            status = WebsiteStatus.BROKEN;
            return;
        }

        if (this.maxUrlDepth < 1) {
            status = WebsiteStatus.NOT_CRAWLED;
            return;
        }

        CrawlHeadings(webDocument);
        CrawlLinkedWebsites(webDocument);
        this.status = WebsiteStatus.OK;
    }

    private void CrawlHeadings(Document document) {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(this.maxHeadingsDepth);
        this.headingsList = HeadingsCrawler.GetHeadingsFromDocument(document, headingLevel);
    }

    private void CrawlLinkedWebsites(Document document) {
        List<String> linkedUrls = LinksCrawler.GetUrlsFromWebsite(document);

        List<WebsiteData> linkedWebsites = CrawlWebsites(linkedUrls, this.maxHeadingsDepth, this.maxUrlDepth - 1);

        if (linkedWebsites.size() > MAX_INNER_WEBSITES_NUM) {
            this.linkedWebsitesList = linkedWebsites.subList(0 , MAX_INNER_WEBSITES_NUM - 1);
        } else {
            this.linkedWebsitesList = linkedWebsites;
        }
    }

    public String getURL() {
        return URL;
    }

    public WebsiteStatus getStatus() {
        return status;
    }

    public List<Heading> getHeadingsList() {
        return headingsList;
    }

    public List<WebsiteData> getLinkedWebsitesList() {
        return linkedWebsitesList;
    }

    public static List<WebsiteData> CrawlWebsites(List<String> URLs, int maxHeadingsDepth, int maxUrlDepth) {
        List<WebsiteData> websiteDataList = new ArrayList<>();

        for (String url : URLs) {
            websiteDataList.add(new WebsiteData(url, maxHeadingsDepth, maxUrlDepth));
        }

        return websiteDataList;
    }

    public enum WebsiteStatus {
        OK, NOT_CRAWLED, BROKEN
    }
}
