package Markdown;

import Data.Heading;
import Data.Website;

import java.util.*;

public class MarkdownWebsiteSummary {

    private final static String NEW_LINE_MD = "  \n";

    public static StringBuilder CreateSummaryForWebsite(Website website, String sourceLanguageName, String targetLanguageName) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(GetBasicInfoMarkdownString(website.getURL(), website.getMaxHeadingsDepth(), website.getMaxUrlDepth(), sourceLanguageName, targetLanguageName));

        WriteWebsiteMarkdownRecursive(website, stringBuilder, 0);

        return stringBuilder;
    }

    public static String GetBasicInfoMarkdownString(String URL, int headingsDepth, int urlDepth, String sourceLanguage, String targetLanguage) {
        StringBuilder sb = new StringBuilder();

        sb.append("Input URL: " + URL + NEW_LINE_MD);
        sb.append("Max URL depth: " + urlDepth + NEW_LINE_MD);
        sb.append("Max headings depth: " + headingsDepth + NEW_LINE_MD);
        sb.append("Source language: " + sourceLanguage +" (auto-detected)" + NEW_LINE_MD);
        sb.append("Target language: " + targetLanguage + NEW_LINE_MD);
        sb.append("Summary:" + NEW_LINE_MD);

        return sb.toString();
    }

    public static StringBuilder GetMarkdownHeadingsStringBuilder(List<Heading> headingsList, int urlDepth) {
        StringBuilder sb = new StringBuilder();

        for (Heading heading : headingsList) {
            sb.append("#".repeat(heading.getLevel().ordinal()));
            sb.append(urlDepth != 0 ? " " : "");
            sb.append("--".repeat(urlDepth));
            sb.append(urlDepth != 0 ? ">" : "");
            sb.append(" ");
            sb.append(heading.getText());
            sb.append("\n");
        }

        sb.append("\n");

        return sb;
    }

    public static boolean WriteWebsiteMarkdownRecursive(Website website, StringBuilder stringBuilder, int initialUrlDepth) {
        if (website == null || stringBuilder == null || initialUrlDepth < 0) {
            return false;
        }

        if (website.getStatus() == Website.WebsiteStatus.BROKEN) {
            if (initialUrlDepth > 0) {
                stringBuilder.append("<br>" + "--".repeat(initialUrlDepth) + "> Broken link: " + website.getURL() + NEW_LINE_MD);
            }
        } else if (website.getStatus() == Website.WebsiteStatus.OK) {
            if (initialUrlDepth > 0) {
                stringBuilder.append("<br>" + "--".repeat(initialUrlDepth) + "> " + website.getURL() + NEW_LINE_MD);
            }

            stringBuilder.append(GetMarkdownHeadingsStringBuilder(website.getHeadingsList(), initialUrlDepth));

            for (Website websiteInner : website.getLinkedWebsitesList()) {
                WriteWebsiteMarkdownRecursive(websiteInner, stringBuilder, initialUrlDepth + 1);
            }
        }

        return true;
    }

    public static List<String> CollectHeadingsFromWebsitesRecursive(Website website) {
        if (website == null || website.getHeadingsList() == null) {
            return new ArrayList<>();
        }

        List<String> headingsStringList = new LinkedList<>();

        for (Heading heading : website.getHeadingsList()) {
            headingsStringList.add(heading.getText());
        }

        for (Website websiteInner : website.getLinkedWebsitesList()) {
            headingsStringList.addAll(CollectHeadingsFromWebsitesRecursive(websiteInner));
        }

        return headingsStringList;
    }

    public static void ApplyHeadingsToWebsitesRecursive(Website website, Queue<String> headingsQueue) {
        if (website == null || website.getHeadingsList() == null || headingsQueue == null) {
            return;
        }

        for (Heading heading : website.getHeadingsList()) {
            heading.setText(headingsQueue.poll());
        }

        for (Website websiteInner : website.getLinkedWebsitesList()) {
            ApplyHeadingsToWebsitesRecursive(websiteInner, headingsQueue);
        }
    }
}
