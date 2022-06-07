package Markdown;

import Crawlers.HeadingsCrawler;
import Data.Heading;
import Data.Website;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownWebsiteSummaryTest {

    @Test
    void createSummaryForWebsite() {
    }

    @Test
    void getBasicInfoMarkdownString() {
        String markdownString = MarkdownWebsiteSummary.GetBasicInfoMarkdownString("MY URL", 3, 1, "English", "Italian");

        StringBuilder sb = new StringBuilder();

        sb.append("Input URL: MY URL  \n");
        sb.append("Max headings depth: 3  \n");
        sb.append("Max websites depth: 1  \n");
        sb.append("Source language: English (auto-detected)  \n");
        sb.append("Target language: Italian  \n");
        sb.append("Summary:  \n");

        assertEquals(sb.toString(), markdownString);
    }

    @Test
    void getMarkdownHeadingsStringBuilder_depthLevelZero_returnsOk() {
        List<Heading> headingsList = new ArrayList<>();
        headingsList.add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        headingsList.add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        headingsList.add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));

        StringBuilder actualHeadings = MarkdownWebsiteSummary.GetMarkdownHeadingsStringBuilder(headingsList, 0);

        StringBuilder expectedHeadings = new StringBuilder();
        expectedHeadings
                .append("# Heading 1\n")
                .append("## Heading 2\n")
                .append("### Heading 3\n")
                .append("\n");

        assertEquals(expectedHeadings.toString(), actualHeadings.toString());
    }

    @Test
    void getMarkdownHeadingsStringBuilder_depthLevelOne_returnsOk() {
        List<Heading> headingsList = new ArrayList<>();
        headingsList.add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        headingsList.add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        headingsList.add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));

        StringBuilder actualHeadings = MarkdownWebsiteSummary.GetMarkdownHeadingsStringBuilder(headingsList, 1);

        StringBuilder expectedHeadings = new StringBuilder();
        expectedHeadings
                .append("# --> Heading 1\n")
                .append("## --> Heading 2\n")
                .append("### --> Heading 3\n")
                .append("\n");

        assertEquals(expectedHeadings.toString(), actualHeadings.toString());
    }

    @Test
    void getMarkdownHeadingsStringBuilder_depthLevelTwo_returnsOk() {
        List<Heading> headingsList = new ArrayList<>();
        headingsList.add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        headingsList.add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        headingsList.add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));

        StringBuilder actualHeadings = MarkdownWebsiteSummary.GetMarkdownHeadingsStringBuilder(headingsList, 2);

        StringBuilder expectedHeadings = new StringBuilder();
        expectedHeadings
                .append("# ----> Heading 1\n")
                .append("## ----> Heading 2\n")
                .append("### ----> Heading 3\n")
                .append("\n");

        assertEquals(expectedHeadings.toString(), actualHeadings.toString());
    }

    @Test
    void writeWebsiteMarkdownRecursive_depthLevel0_returnsOk() {
        StringBuilder actualStringBuilder = new StringBuilder();
        StringBuilder expectedStringBuilder = new StringBuilder();
        boolean isOk = MarkdownWebsiteSummary.WriteWebsiteMarkdownRecursive(prepareSimpleWebsiteOk(), actualStringBuilder, 0);

        expectedStringBuilder
                .append(MarkdownWebsiteSummary.GetMarkdownHeadingsStringBuilder(prepareSimpleWebsiteOk().getHeadingsList(), 0));

        assertEquals(expectedStringBuilder.toString(), actualStringBuilder.toString());
        assertTrue(isOk);
    }

    @Test
    void writeWebsiteMarkdownRecursive_depthLevel1_returnsOk() {
        StringBuilder actualStringBuilder = new StringBuilder();
        StringBuilder expectedStringBuilder = new StringBuilder();
        boolean isOk = MarkdownWebsiteSummary.WriteWebsiteMarkdownRecursive(prepareSimpleWebsiteOk(), actualStringBuilder, 1);

        expectedStringBuilder
                .append("<br>--> MY URL  \n")
                .append(MarkdownWebsiteSummary.GetMarkdownHeadingsStringBuilder(prepareSimpleWebsiteOk().getHeadingsList(), 1));

        assertEquals(expectedStringBuilder.toString(), actualStringBuilder.toString());
        assertTrue(isOk);
    }

    private Website prepareSimpleWebsiteOk() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        List<Heading> headingsList = new ArrayList<>();
        headingsList.add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        headingsList.add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        headingsList.add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));
        website.setHeadingsList(headingsList);

        return website;
    }

    private Website prepareOneLevelSimpleWebsiteOk() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        website.getHeadingsList().add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        website.getHeadingsList().add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        website.getHeadingsList().add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));

        Website websiteInner = new Website("MY URL 2", Website.WebsiteStatus.OK, 3);
        websiteInner.setStatus(Website.WebsiteStatus.OK);

        websiteInner.getHeadingsList().add(new Heading(Heading.HeadingLevel.H1, "Heading 1"));
        websiteInner.getHeadingsList().add(new Heading(Heading.HeadingLevel.H2, "Heading 2"));
        websiteInner.getHeadingsList().add(new Heading(Heading.HeadingLevel.H3, "Heading 3"));

        website.getLinkedWebsitesList().add(websiteInner);

        return website;
    }
}