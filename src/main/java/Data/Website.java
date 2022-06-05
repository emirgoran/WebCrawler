package Data;

import java.util.ArrayList;
import java.util.List;

public class Website {

    public enum WebsiteStatus {
        OK, NOT_CRAWLED, BROKEN
    }

    private String URL;
    private List<Heading> headingsList;
    private List<Website> linkedWebsitesList;

    private WebsiteStatus status;
    private int maxHeadingsDepth;
    private int maxUrlDepth;


    public Website(String URL, int maxHeadingsDepth, int maxUrlDepth) {
        this.URL = URL;
        this.headingsList = new ArrayList<>();
        this.linkedWebsitesList = new ArrayList<>();

        this.status = WebsiteStatus.BROKEN;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.maxUrlDepth = maxUrlDepth;
    }

    public String getURL() {
        return URL;
    }

    public WebsiteStatus getStatus() {
        return status;
    }

    public List<Heading> getHeadingsList() {
        return headingsList;
    }

    public List<Website> getLinkedWebsitesList() {
        return linkedWebsitesList;
    }

    public int getMaxUrlDepth() {
        return maxUrlDepth;
    }

    public int getMaxHeadingsDepth() {
        return maxHeadingsDepth;
    }

    public void setHeadingsList(List<Heading> headingsList) {
        this.headingsList = headingsList;
    }

    public void setLinkedWebsitesList(List<Website> linkedWebsitesList) {
        this.linkedWebsitesList = linkedWebsitesList;
    }

    public void setStatus(WebsiteStatus status) {
        this.status = status;
    }
}
