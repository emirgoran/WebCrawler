package Data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebElementTest {

    @Test
    void getTag() {
        WebElement webElement = new WebElement("TAG", "TEXT");

        assertEquals("TAG", webElement.getTag());
    }

    @Test
    void getText() {
        WebElement webElement = new WebElement("TAG", "TEXT");

        assertEquals("TEXT", webElement.getText());
    }
}