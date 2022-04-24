package Crawlers;

import Data.Heading;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class HeadingsCrawlerTest {

    @Test
    void getHeadingsFromDocument_testNoHeadingLevels_returnsOk() {
        Document document = new Document("localhost");

        List<Heading> headingsList = HeadingsCrawler.GetHeadingsFromDocument(document, Heading.HeadingLevel.H6);

        Assertions.assertEquals(0, headingsList.size());
    }

    @Test
    void getHeadingsFromDocument_testAllHeadingLevels_returnsOk() {
        Document document = new Document("localhost");
        document.append("<h0> Heading 0 </h0>"); // invalid one
        document.append("<h1> Heading 1 </h1>");
        document.append("<h2> Heading 2 </h2>");
        document.append("<h3> Heading 3 </h3>");
        document.append("<h4> Heading 4 </h4>");
        document.append("<h5> Heading 5 </h5>");
        document.append("<h6> Heading 6 </h6>");
        document.append("<h7> Heading 7 </h7>"); // invalid one

        List<Heading> headingsList = HeadingsCrawler.GetHeadingsFromDocument(document, Heading.HeadingLevel.H6);

        Assertions.assertEquals(6, headingsList.size());
        for (int i = 0; i < 6; i++) {
            Assertions.assertEquals("Heading " + (i+1), headingsList.get(i).getText());
            Assertions.assertEquals(HeadingsCrawler.GetHeadingLevelFromInt(i+1), headingsList.get(i).getLevel());
        }
    }

    @Test
    void getCssHeadingsQuery_testLevelZero_returnsEmptyString() {
        String cssQuery = HeadingsCrawler.GetCssHeadingsQuery(Heading.HeadingLevel.H0);

        Assertions.assertEquals("", cssQuery);
    }

    @Test
    void getCssHeadingsQuery_testLevelOne_returnsOk() {
        String cssQuery = HeadingsCrawler.GetCssHeadingsQuery(Heading.HeadingLevel.H1);

        Assertions.assertEquals("h1", cssQuery);
    }

    @Test
    void getCssHeadingsQuery_testLevelSix_returnsOk() {
        String cssQuery = HeadingsCrawler.GetCssHeadingsQuery(Heading.HeadingLevel.H6);

        Assertions.assertEquals("h1, h2, h3, h4, h5, h6", cssQuery);
    }

    @Test
    void getHeadingLevelFromTag_invalidTag_returnsHeadingLevelZero() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromTag("h7");

        Assertions.assertEquals(Heading.HeadingLevel.H0, headingLevel);
    }

    @Test
    void getHeadingLevelFromTag_level1_returnsOk() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromTag("h1");

        Assertions.assertEquals(Heading.HeadingLevel.H1, headingLevel);
    }

    @Test
    void getHeadingLevelFromTag_level6_returnsOk() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromTag("h6");

        Assertions.assertEquals(Heading.HeadingLevel.H6, headingLevel);
    }

    @Test
    void getHeadingLevelFromInt_level0_returnsOk() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(0);

        Assertions.assertEquals(Heading.HeadingLevel.H0, headingLevel);
    }

    @Test
    void getHeadingLevelFromInt_level1_returnsOk() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(1);

        Assertions.assertEquals(Heading.HeadingLevel.H1, headingLevel);
    }

    @Test
    void getHeadingLevelFromInt_level6_returnsOk() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(6);

        Assertions.assertEquals(Heading.HeadingLevel.H6, headingLevel);
    }

    @Test
    void getHeadingLevelFromInt_invalidLevel7_returnsLevel0() {
        Heading.HeadingLevel headingLevel = HeadingsCrawler.GetHeadingLevelFromInt(7);

        Assertions.assertEquals(Heading.HeadingLevel.H0, headingLevel);
    }
}