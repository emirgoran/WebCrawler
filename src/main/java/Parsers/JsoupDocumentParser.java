package Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupDocumentParser implements DocumentParser {

    /* Not unit testable. Requires the URL to be valid and accessible! */
    public Document ParseUrl(String URL) throws IOException {
        return Jsoup.connect(URL).get();
    }
}
