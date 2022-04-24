package Crawlers;

import Data.Website;

import java.util.List;

public interface WebsiteCrawler {

    void CrawlWebsite();

    List<Website> CrawlWebsites(List<String> URLs, int maxHeadingsDepth, int maxUrlDepth);
}
