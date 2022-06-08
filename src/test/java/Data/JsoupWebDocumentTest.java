package Data;

import org.jsoup.nodes.Document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

class JsoupWebDocumentTest {

    JsoupWebDocument jsoupWebDocument;

    @BeforeEach
    public void setUp() {
        Document document = new Document("");

        document.append("<h1> Heading 1 </h1>");
        document.append("<h2> Heading 2 </h2>");
        document.append("<h3> Heading 3 </h3>");
        document.append("<h4> Heading 4 </h4>");
        document.append("<h5> Heading 5 </h5>");
        document.append("<h6> Heading 6 </h6>");

        document.append("<a href=\"https://www.github.com\"> GitHub </a>");
        document.append("<a href=\"https://www.twitter.com\"> Twitter </a>");
        document.append("<a href=\"www.wikipedia.org\"> Wikipedia </a>"); // Without an absolute URL
        document.append("<a> No href here </a>"); // One without URL

        jsoupWebDocument = new JsoupWebDocument(document);
    }

    @Test
    void selectElementsFromWebDocument_testHeading1_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h1");

        assertEquals(1, webElements.size());
        assertEquals("h1", webElements.get(0).getTag());
        assertEquals("Heading 1", webElements.get(0).getText());
    }

    @Test
    void selectElementsFromWebDocument_testHeading6_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h6");

        assertEquals(1, webElements.size());
        assertEquals("h6", webElements.get(0).getTag());
        assertEquals("Heading 6", webElements.get(0).getText());
    }

    @Test
    void selectElementsFromWebDocument_testHeadingNonExisting_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h0");

        assertEquals(0, webElements.size());
    }

    @Test
    void selectElementsFromWebDocument_testHeadingAll_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h1, h2, h3, h4, h5, h6");

        assertEquals(6, webElements.size());

        for (int i = 1; i < 7; i++) {
            assertEquals("h" + i, webElements.get(i - 1).getTag());
            assertEquals("Heading " + i, webElements.get(i - 1).getText());
        }
    }

    @Test
    void selectElementsFromWebDocument_testHeadingAllAndNonExistingHeadings_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h0, h1, h2, h3, h4, h5, h6, h7");

        assertEquals(6, webElements.size());

        for (int i = 1; i < 7; i++) {
            assertEquals("h" + i, webElements.get(i - 1).getTag());
            assertEquals("Heading " + i, webElements.get(i - 1).getText());
        }
    }

    @Test
    void selectElementsFromWebDocument_testGetAllAElements_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("a");

        assertEquals(4, webElements.size());

        for (int i = 0; i < 4; i++) {
            assertEquals("a", webElements.get(i).getTag());
        }

        assertEquals("GitHub", webElements.get(0).getText());
        assertEquals("Twitter", webElements.get(1).getText());
        assertEquals("Wikipedia", webElements.get(2).getText());
        assertEquals("No href here", webElements.get(3).getText());
    }

    @Test
    void selectElementsFromWebDocument_testGetAllAElementsWithHref_returnsOk() {
        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("a[href]");

        assertEquals(3, webElements.size());

        for (int i = 0; i < 3; i++) {
            assertEquals("a", webElements.get(i).getTag());
        }

        assertEquals("GitHub", webElements.get(0).getText());
        assertEquals("Twitter", webElements.get(1).getText());
        assertEquals("Wikipedia", webElements.get(2).getText());
    }

    @Test
    void getAbsoluteUrlsFromWebDocument_testAbsoluteUrlsGet_returnsOk() {
        List<String> urls = jsoupWebDocument.getAbsoluteUrlsFromWebDocument();

        assertEquals(3, urls.size());
        assertEquals("https://www.github.com", urls.get(0));
        assertEquals("https://www.twitter.com", urls.get(1));
        assertEquals("", urls.get(2));
    }

    @Test
    void append() {
        jsoupWebDocument.append("<h7> Heading 7 </h7>");

        List<WebElement> webElements = jsoupWebDocument.selectElementsFromWebDocument("h7");

        assertEquals(1, webElements.size());
        assertEquals("h7", webElements.get(0).getTag());
        assertEquals("Heading 7", webElements.get(0).getText());
    }
}