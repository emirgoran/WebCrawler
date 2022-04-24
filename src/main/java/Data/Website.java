package Data;

import Crawlers.HeadingsCrawler;
import Crawlers.LinksCrawler;
import Crawlers.WebsiteCrawler;
import Parsers.ArgumentsParser;
import Parsers.DocumentParser;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class Website implements WebsiteCrawler {

    public static final int MAX_INNER_WEBSITES_NUM = 5;

    public enum WebsiteStatus {
        OK, NOT_CRAWLED, BROKEN
    }

    private DocumentParser documentParser;
    private String URL;
    private List<Heading> headingsList;
    private List<Website> linkedWebsitesList;

    private WebsiteStatus status;
    private int maxHeadingsDepth;
    private int maxUrlDepth;


    public Website(DocumentParser documentParser, String URL, int maxHeadingsDepth, int maxUrlDepth) {
        this.documentParser = documentParser;
        this.URL = URL;
        this.headingsList = new ArrayList<>();
        this.linkedWebsitesList = new ArrayList<>();

        this.status = WebsiteStatus.BROKEN;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.maxUrlDepth = maxUrlDepth;
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

    public List<Website> getLinkedWebsitesList() {
        return linkedWebsitesList;
    }

    public int getMaxUrlDepth() {
        return maxUrlDepth;
    }

    public int getMaxHeadingsDepth() {
        return maxHeadingsDepth;
    }

    public void setHeadingsList(List<Heading> headingsList) {
        this.headingsList = headingsList;
    }

    public void setLinkedWebsitesList(List<Website> linkedWebsitesList) {
        this.linkedWebsitesList = linkedWebsitesList;
    }

    public void setStatus(WebsiteStatus status) {
        this.status = status;
    }

    public void CrawlWebsite() {
        Document webDocument = documentParser.ParseUrl(this.URL);

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

    /* Get headings from the web document. */
    private void CrawlHeadings(Document document) {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(this.maxHeadingsDepth);
        this.headingsList = HeadingsCrawler.GetHeadingsFromDocument(document, headingLevel);
    }

    /* Find URLs on a webpage, then recursively crawl those URLs until the maxUrlDepth is reached. */
    /* Also limit the number of linked websites due to the number of websites getting out of control. */
    private void CrawlLinkedWebsites(Document document) {
        List<String> linkedUrls = LinksCrawler.GetUrlsFromWebsite(document);

        List<Website> linkedWebsites = CrawlWebsites(linkedUrls, this.maxHeadingsDepth, this.maxUrlDepth - 1);

        if (linkedWebsites.size() > MAX_INNER_WEBSITES_NUM) {
            this.linkedWebsitesList = linkedWebsites.subList(0 , MAX_INNER_WEBSITES_NUM - 1);
        } else {
            this.linkedWebsitesList = linkedWebsites;
        }
    }

    /* Recursively crawl websites until the maxUrlDepth is reached. */
    public List<Website> CrawlWebsites(List<String> URLs, int maxHeadingsDepth, int maxUrlDepth) {
        List<Website> websiteList = new ArrayList<>();

        for (String url : URLs) {
            Website website = new Website(documentParser, url, maxHeadingsDepth, maxUrlDepth);
            website.CrawlWebsite();
            websiteList.add(website);
        }

        return websiteList;
    }
}
