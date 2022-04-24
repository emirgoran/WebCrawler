package Crawlers;

import Data.WebsiteData;

import java.util.List;

public interface WebsiteCrawler {

    void CrawlWebsite();

    List<WebsiteData> CrawlWebsites(List<String> URLs, int maxHeadingsDepth, int maxUrlDepth);
}
