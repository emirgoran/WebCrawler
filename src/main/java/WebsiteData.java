import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class WebsiteData {
    String URL;
    List<Heading> headings;
    List<WebsiteData> linkedWebsites;
    int maxUrlDepth;
    int maxHeadingsDepth;

    public WebsiteData(String URL, int maxHeadingsDepth, int maxUrlDepth) {
        this.URL = URL;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.maxUrlDepth = maxUrlDepth;

        CrawlWebsite();
    }

    private void CrawlWebsite() {
        if (this.maxUrlDepth < 1) {
            return;
        }

        Document website = ArgumentsParser.ParseUrl(this.URL);

        if (website == null) {
            return;
        }

        this.headings = HeadingsCrawler.GetHeadingsFromDocument(website, HeadingsCrawler.GetHeadingLevelFromInt(maxHeadingsDepth));

        List<String> linkedURLs = LinksCrawler.GetUrlsFromWebsite(website);
        this.linkedWebsites = CrawlWebsites(linkedURLs, maxHeadingsDepth, maxUrlDepth - 1);

    }

    public static List<WebsiteData> CrawlWebsites(List<String> URLs, int maxHeadingsDepth, int maxUrlDepth) {
        List<WebsiteData> websiteDataList = new ArrayList<>();

        for (String url : URLs) {
            websiteDataList.add(new WebsiteData(url, maxHeadingsDepth, maxUrlDepth));
        }

        return websiteDataList;
    }
}
