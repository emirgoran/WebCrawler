import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MarkdownWebsiteSummary {

    private final static String DEFAULT_SUMMARY_FILE_PATH = "C:\\MarkdownSummaryWeb";

    public static void CreateSummaryForWebsite(String URL, int maxHeadingsDepth, int maxUrlDepth, String targetLanguage) throws IOException {
        FileWriter summaryFileWriter = new FileWriter(DEFAULT_SUMMARY_FILE_PATH, true);
        summaryFileWriter.write(GetBasicInfoMarkdownString(URL, maxHeadingsDepth, maxUrlDepth, targetLanguage));

        WebsiteData websiteData = new WebsiteData(URL, maxHeadingsDepth, maxUrlDepth);
    }

    private static String GetBasicInfoMarkdownString(String URL, int headingsDepth, int urlDepth, String targetLanguage) {
        StringBuilder sb = new StringBuilder();

        sb.append("Input URL: " + URL + "\n");
        sb.append("<br>Max URL depth: " + urlDepth + "\n");
        sb.append("<br>Max headings depth: " + headingsDepth + "\n");
        sb.append("<br>Source language: auto-detect\n");
        sb.append("<br>Target language: " + targetLanguage + "\n");

        return sb.toString();
    }

    private static void PrintHeadings(List<Heading> headingsList, int currentUrlDepth) {
        StringBuilder sb = new StringBuilder();

        for (Heading heading : headingsList) {
            sb.append("#".repeat(heading.getLevel().ordinal()));
            sb.append(" ");
            sb.append("--".repeat(currentUrlDepth));
            sb.append(currentUrlDepth != 0 ? ">" : "");
            sb.append(" ");
            sb.append(heading.getText());
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

}
