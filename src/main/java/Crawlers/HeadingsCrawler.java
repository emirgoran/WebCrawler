package Crawlers;

import Data.Heading;
import Data.WebDocument;
import Data.WebElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeadingsCrawler {

    public static List<Heading> GetHeadingsFromWebDocument(WebDocument webDocument, Heading.HeadingLevel depth) {
        List<Heading> headingList = new ArrayList<>();

        if (Heading.HeadingLevel.H0.equals(depth)) {
            return headingList;
        }

        List<WebElement> hTags = webDocument.selectElementsFromWebDocument(GetCssHeadingsQuery(depth));

        for (WebElement webElement : hTags) {
            headingList.add(new Heading(GetHeadingLevelFromTag(webElement.getTag()), webElement.getText()));
        }

        return headingList;
    }

    public static String GetCssHeadingsQuery(Heading.HeadingLevel depth) {
        Set<String> headings = new HashSet<>();

        for (int i = 1; i <= depth.ordinal(); i++) {
            headings.add("h" + i);
        }

        return String.join(", ", headings);
    }

    public static Heading.HeadingLevel GetHeadingLevelFromTag(String tag) {
        switch (tag) {
            case "h1": return Heading.HeadingLevel.H1;
            case "h2": return Heading.HeadingLevel.H2;
            case "h3": return Heading.HeadingLevel.H3;
            case "h4": return Heading.HeadingLevel.H4;
            case "h5": return Heading.HeadingLevel.H5;
            case "h6": return Heading.HeadingLevel.H6;
            default: return Heading.HeadingLevel.H0;
        }
    }

    public static Heading.HeadingLevel GetHeadingLevelFromInt(int headingLevel) {
        return GetHeadingLevelFromTag("h" + headingLevel);
    }
}
