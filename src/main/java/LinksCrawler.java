import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class LinksCrawler {

    public static final int MAX_LINKED_WEBSITES = 3;

    public static List<String> GetUrlsFromWebsite(Document document) {
        List<String> urls = new ArrayList<>();
        int pagesCount = 0;

        Elements links = document.select("a[href]");

        for (Element link : links) {
            urls.add(link.absUrl("href"));
            System.out.println(link.absUrl("href"));

            if (++pagesCount >= MAX_LINKED_WEBSITES) {
                break;
            }
        }

        return urls;
    }

}
