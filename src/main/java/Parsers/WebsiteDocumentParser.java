package Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebsiteDocumentParser implements DocumentParser {

    /* Not unit testable. Requires the URL to be valid and accessible! */
    public Document ParseUrl(String URL)
    {
        try {
            return Jsoup.connect(URL).get();
        } catch (Exception e) {
            return null;
        }
    }
}
