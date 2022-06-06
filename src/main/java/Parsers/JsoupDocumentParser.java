package Parsers;

import Data.JsoupWebDocument;
import Data.WebDocument;
import org.jsoup.Jsoup;

import java.io.IOException;

public class JsoupDocumentParser implements DocumentParser {

    /* Not unit testable. Requires the URL to be valid and accessible! */
    public WebDocument ParseUrl(String URL) throws IOException {
        return new JsoupWebDocument(Jsoup.connect(URL).get());
    }
}
