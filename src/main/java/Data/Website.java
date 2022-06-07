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
    private Translation linkedTranslation;

    public Website(String URL, WebsiteStatus initialStatus, int maxHeadingsDepth) {
        this.URL = URL;
        this.headingsList = new ArrayList<>();
        this.linkedWebsitesList = new ArrayList<>();
        this.status = initialStatus;
        this.maxHeadingsDepth = maxHeadingsDepth;
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

    public int getMaxHeadingsDepth() {
        return maxHeadingsDepth;
    }

    public void setHeadingsList(List<Heading> headingsList) {
        this.headingsList = headingsList;
    }

    public void setStatus(WebsiteStatus status) {
        this.status = status;
    }

    public Translation getLinkedTranslation() {
        return linkedTranslation;
    }

    public void setLinkedTranslation(Translation linkedTranslation) {
        this.linkedTranslation = linkedTranslation;
    }
}
