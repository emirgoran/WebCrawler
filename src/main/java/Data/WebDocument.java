package Data;

import java.util.List;

public interface WebDocument {
    List<WebElement> selectElementsFromWebDocument(String cssQuery);

    List<String> getAbsoluteUrlsFromDocument();

    void append(String s);
}
