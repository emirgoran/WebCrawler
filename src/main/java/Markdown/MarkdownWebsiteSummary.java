package Markdown;

import Data.Heading;
import Data.WebsiteData;
import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;

import java.util.*;

public class MarkdownWebsiteSummary {

    private final static String NEW_LINE_MD = "  \n";

    public static StringBuilder CreateSummaryForWebsite(WebsiteData websiteData, String sourceLanguageName, String targetLanguageName)
            throws TranslationInvalidArgumentException, TranslationNotSuccessfulException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(GetBasicInfoMarkdownString(websiteData.getURL(), websiteData.getMaxHeadingsDepth(), websiteData.getMaxUrlDepth(), sourceLanguageName, targetLanguageName));

        WriteWebsiteMarkdownRecursive(websiteData, stringBuilder, 0);

        return stringBuilder;
    }

    public static String GetBasicInfoMarkdownString(String URL, int headingsDepth, int urlDepth, String sourceLanguage, String targetLanguage) {
        StringBuilder sb = new StringBuilder();

        sb.append("Input URL: " + URL + NEW_LINE_MD);
        sb.append("Max URL depth: " + urlDepth + NEW_LINE_MD);
        sb.append("Max headings depth: " + headingsDepth + NEW_LINE_MD);
        sb.append("Source language: " + sourceLanguage +" (auto-detected)" + NEW_LINE_MD);
        sb.append("Target language: " + targetLanguage + NEW_LINE_MD);
        sb.append("Summary: " + NEW_LINE_MD);

        return sb.toString();
    }

    public static StringBuilder GetMarkdownHeadingsStringBuilder(List<Heading> headingsList, int urlDepth) {
        StringBuilder sb = new StringBuilder();

        for (Heading heading : headingsList) {
            sb.append("#".repeat(heading.getLevel().ordinal()));
            sb.append(" ");
            sb.append("--".repeat(urlDepth));
            sb.append(urlDepth != 0 ? ">" : "");
            sb.append(" ");
            sb.append(heading.getText());
            sb.append("\n");
        }

        sb.append("\n");

        return sb;
    }

    public static boolean WriteWebsiteMarkdownRecursive(WebsiteData websiteData, StringBuilder stringBuilder, int initialUrlDepth) {
        if (websiteData == null || stringBuilder == null || initialUrlDepth < 0) {
            return false;
        }

        if (websiteData.getStatus() == WebsiteData.WebsiteStatus.BROKEN) {
            if (initialUrlDepth > 0) {
                stringBuilder.append("<br>" + "--".repeat(initialUrlDepth) + "> Broken link: " + websiteData.getURL() + NEW_LINE_MD);
            }
        } else if (websiteData.getStatus() == WebsiteData.WebsiteStatus.OK) {
            if (initialUrlDepth > 0) {
                stringBuilder.append("<br>" + "--".repeat(initialUrlDepth) + "> " + websiteData.getURL() + NEW_LINE_MD);
            }

            stringBuilder.append(GetMarkdownHeadingsStringBuilder(websiteData.getHeadingsList(), initialUrlDepth));

            for (WebsiteData websiteDataInner : websiteData.getLinkedWebsitesList()) {
                WriteWebsiteMarkdownRecursive(websiteDataInner, stringBuilder, initialUrlDepth + 1);
            }
        }

        return true;
    }

    public static List<String> CollectHeadingsFromWebsitesRecursive(WebsiteData websiteData) {
        if (websiteData == null || websiteData.getHeadingsList() == null) {
            return new ArrayList<>();
        }

        List<String> headingsStringList = new LinkedList<>();

        for (Heading heading : websiteData.getHeadingsList()) {
            headingsStringList.add(heading.getText());
        }

        for (WebsiteData websiteDataInner : websiteData.getLinkedWebsitesList()) {
            headingsStringList.addAll(CollectHeadingsFromWebsitesRecursive(websiteDataInner));
        }

        return headingsStringList;
    }

    public static void ApplyHeadingsToWebsitesRecursive(WebsiteData websiteData, Queue<String> headingsQueue) {
        if (websiteData == null || websiteData.getHeadingsList() == null || headingsQueue == null) {
            return;
        }

        for (Heading heading : websiteData.getHeadingsList()) {
            heading.setText(headingsQueue.poll());
        }

        for (WebsiteData websiteDataInner : websiteData.getLinkedWebsitesList()) {
            ApplyHeadingsToWebsitesRecursive(websiteDataInner, headingsQueue);
        }
    }
}
