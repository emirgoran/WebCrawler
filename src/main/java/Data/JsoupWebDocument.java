package Data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupWebDocument implements WebDocument {

    private Document document;

    public JsoupWebDocument(Document document) {
        this.document = document;
    }

    @Override
    public List<WebElement> selectElementsFromDocument(String cssQuery) {
        Elements elements = document.select(cssQuery);
        ArrayList<WebElement> webElements = new ArrayList<>(elements.size());

        for (Element element : elements) {
            webElements.add(new WebElement(element.tagName(), element.ownText()));
        }

        return webElements;
    }

    @Override
    public List<String> getAbsoluteUrlsFromDocument() {
        ArrayList<String> urls = new ArrayList<>();

        Elements links = document.select("a[href]");

        for (Element link : links) {
            // Get absolute links for completeness reasons.
            urls.add(link.absUrl("href"));
        }

        return urls;
    }

    @Override
    public void append(String s) {
        document.append(s);
    }
}
