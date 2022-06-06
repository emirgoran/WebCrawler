package Data;

public class WebElement {
    private String tag;
    private String text;

    public WebElement(String tag, String text) {
        this.tag = tag;
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }
}
