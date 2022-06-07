package Data;

import Translation.Translation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebsiteTest {

    @Test
    void getURL() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertEquals("MY URL", website.getURL());
    }

    @Test
    void getStatus() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertEquals(Website.WebsiteStatus.OK, website.getStatus());
    }

    @Test
    void getHeadingsList() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertEquals(0, website.getHeadingsList().size());
    }

    @Test
    void getLinkedWebsitesList() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertEquals(0, website.getLinkedWebsitesList().size());
    }

    @Test
    void getMaxHeadingsDepth() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertEquals(3, website.getMaxHeadingsDepth());
    }

    @Test
    void setHeadingsList() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        website.setHeadingsList(null);

        assertNull(website.getHeadingsList());
    }

    @Test
    void setStatus() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        website.setStatus(Website.WebsiteStatus.BROKEN);

        assertEquals(Website.WebsiteStatus.BROKEN, website.getStatus());
    }

    @Test
    void getLinkedTranslation() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);

        assertNull(website.getLinkedTranslation());
    }

    @Test
    void setLinkedTranslation() {
        Website website = new Website("MY URL", Website.WebsiteStatus.OK, 3);
        Translation translation = new Translation(null, null, null, null);

        website.setLinkedTranslation(translation);

        assertSame(translation, website.getLinkedTranslation());
    }
}