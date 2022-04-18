import org.jsoup.nodes.Document;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;

public class WebsiteData {
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
        Document website = ArgumentsParser.ParseUrl(this.URL);

        if (website == null) {
            status = WebsiteStatus.BROKEN;
            return;
        }

        if (this.maxUrlDepth < 1) {
            status = WebsiteStatus.NOT_CRAWLED;
            return;
        }

        this.headingsList = HeadingsCrawler.GetHeadingsFromDocument(website, HeadingsCrawler.GetHeadingLevelFromInt(maxHeadingsDepth));
        this.linkedWebsitesList = CrawlWebsites(LinksCrawler.GetUrlsFromWebsite(website), maxHeadingsDepth, maxUrlDepth - 1);
        this.status = WebsiteStatus.OK;
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
