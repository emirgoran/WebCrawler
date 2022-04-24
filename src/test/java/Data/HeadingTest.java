package Data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeadingTest {

    @Test
    void getLevel() {
        Heading heading = new Heading(Heading.HeadingLevel.H1, "Heading 1");

        Assertions.assertEquals(Heading.HeadingLevel.H1, heading.getLevel());
    }

    @Test
    void getText() {
        Heading heading = new Heading(Heading.HeadingLevel.H1, "Heading 1");

        Assertions.assertEquals("Heading 1", heading.getText());
    }

    @Test
    void setText() {
        Heading heading = new Heading(Heading.HeadingLevel.H1, "Heading 1");

        heading.setText("Heading 1 altered");

        Assertions.assertEquals("Heading 1 altered", heading.getText());
    }
}