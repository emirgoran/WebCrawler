import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MarkdownWebsiteSummary {

    private final static String DEFAULT_SUMMARY_FILE_PATH = "C:\\MarkdownSummaryWeb\\summary.md";
    private final static String NEW_LINE_MD = "  \n";

    public static void CreateSummaryForWebsite(String URL, int maxHeadingsDepth, int maxUrlDepth, String targetLanguage) throws IOException {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH);

        summaryFileWriter.write(GetBasicInfoMarkdownString(URL, maxHeadingsDepth, maxUrlDepth, targetLanguage));

        WebsiteData websiteData = new WebsiteData(URL, maxHeadingsDepth, maxUrlDepth);

        TranslateWebsitesRecursively(websiteData);

        WriteWebsiteMarkdownRecursive(websiteData, summaryFileWriter, 0);

        summaryFileWriter.close();
    }

    private static String GetBasicInfoMarkdownString(String URL, int headingsDepth, int urlDepth, String targetLanguage) {
        StringBuilder sb = new StringBuilder();

        sb.append("Input URL: " + URL + NEW_LINE_MD);
        sb.append("Max URL depth: " + urlDepth + NEW_LINE_MD);
        sb.append("Max headings depth: " + headingsDepth + NEW_LINE_MD);
        sb.append("Source language: auto-detect" + NEW_LINE_MD);
        sb.append("Target language: " + targetLanguage + NEW_LINE_MD);
        sb.append("Summary: " + NEW_LINE_MD);

        return sb.toString();
    }

    private static void WriteMarkdownHeadings(FileWriter fileWriter, List<Heading> headingsList, int urlDepth) throws IOException {
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
        fileWriter.write(sb.toString());
    }

    public static boolean WriteWebsiteMarkdownRecursive(WebsiteData websiteData, FileWriter fileWriter, int initialUrlDepth) throws IOException {
        if (websiteData == null || fileWriter == null || initialUrlDepth < 0) {
            return false;
        }

        if (websiteData.getStatus() == WebsiteData.WebsiteStatus.BROKEN) {
            if (initialUrlDepth > 0) {
                fileWriter.write("<br>" + "--".repeat(initialUrlDepth) + "> Broken link: " + websiteData.getURL() + NEW_LINE_MD);
            }
        } else if (websiteData.getStatus() == WebsiteData.WebsiteStatus.OK) {
            if (initialUrlDepth > 0) {
                fileWriter.write("<br>" + "--".repeat(initialUrlDepth) + "> " + websiteData.getURL() + NEW_LINE_MD);
            }

            WriteMarkdownHeadings(fileWriter, websiteData.getHeadingsList(), initialUrlDepth);

            for (WebsiteData websiteDataInner : websiteData.getLinkedWebsitesList()) {
                WriteWebsiteMarkdownRecursive(websiteDataInner, fileWriter, initialUrlDepth + 1);
            }
        }

        return true;
    }

    public static String CollectHeadingsFromWebsitesRecursive(WebsiteData websiteData) {
        if (websiteData == null || websiteData.getHeadingsList() == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Heading heading : websiteData.getHeadingsList()) {
            sb.append(heading.getText() + "\n");
        }

        for (WebsiteData websiteDataInner : websiteData.getLinkedWebsitesList()) {
            sb.append(CollectHeadingsFromWebsitesRecursive(websiteDataInner));
        }

        return sb.toString();
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

    private static void TranslateWebsitesHeadingsRecursively(WebsiteData websiteData) {
        String collectedHeadings = CollectHeadingsFromWebsitesRecursive(websiteData);

        collectedHeadings = collectedHeadings
                .replace('e', '3')
                .replace('a', '4')
                .replace('a', '4')
                .replace('i', '1')
                .replace('g', '9')
                .replace('s', '5');

        String[] headingsArray = collectedHeadings.split("\n");
        Queue<String> headingsQueue = new LinkedList(Arrays.asList(headingsArray));
        ApplyHeadingsToWebsitesRecursive(websiteData, headingsQueue);
    }

}
