package Data;

import Translation.Translation;

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

    public Translation getLinkedTranslation() {
        return linkedTranslation;
    }

    public void setLinkedTranslation(Translation linkedTranslation) {
        this.linkedTranslation = linkedTranslation;
    }

    private Translation linkedTranslation;


    public Website(String URL, int maxHeadingsDepth, int maxUrlDepth) {
        this.URL = URL;
        this.headingsList = new ArrayList<>();
        this.linkedWebsitesList = new ArrayList<>();

        this.status = WebsiteStatus.BROKEN;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.maxUrlDepth = maxUrlDepth;
    }

    public Website(String URL, WebsiteStatus initialStatus, int maxHeadingsDepth) {
        this.URL = URL;
        this.status = initialStatus;
        this.maxHeadingsDepth = maxHeadingsDepth;
        this.headingsList = new ArrayList<>();
        this.linkedWebsitesList = new ArrayList<>();
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

    public void setMaxUrlDepth(int maxUrlDepth) {
        this.maxUrlDepth = maxUrlDepth;
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
