package Data;

public class Heading {
    private HeadingLevel level;
    private String text;

    public Heading(HeadingLevel level, String text) {
        this.level = level;
        this.text = text;
    }

    public HeadingLevel getLevel() {
        return level;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public enum HeadingLevel {
        H0, H1, H2, H3, H4, H5, H6
    }

    @Override
    public String toString() {
        return getText();
    }
}

