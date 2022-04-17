import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeadingsCrawler {

    public static List<Heading> GetHeadingsFromDocument(Document document, Heading.HeadingLevel depth) {
        List<Heading> headingList = new ArrayList<>();

        Elements hTags = document.select(GetCssHeadingsQuery(depth));

        for (Element element : hTags) {
            headingList.add(new Heading(GetHeadingLevelFromTag(element.tagName()), element.ownText()));
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
