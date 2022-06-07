package Parsers;

import Data.JsoupWebDocument;
import Translation.Translator;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArgumentsParserTest {

    @Test
    void isSupportedLanguageCode_notSupported_returnsFalse() {
        Translator translator = mock(Translator.class);
        when(translator.IsSupportedTargetLanguageCode(any())).thenReturn(false);
        boolean result = ArgumentsParser.IsSupportedLanguageCode(translator, "EN");

        assertFalse(result);
    }

    @Test
    void isSupportedLanguageCode_isSupported_returnsTrue() {
        Translator translator = mock(Translator.class);
        when(translator.IsSupportedTargetLanguageCode(any())).thenReturn(true);
        boolean result = ArgumentsParser.IsSupportedLanguageCode(translator, "EN");

        assertTrue(result);
    }

    @Test
    void parseDepth_Zero_ok() {
        Integer result = ArgumentsParser.ParseDepth("0");

        assertEquals(0, result);
    }

    @Test
    void parseDepth_negativeOne_ok() {
        Integer result = ArgumentsParser.ParseDepth("-1");

        assertEquals(-1, result);
    }

    @Test
    void parseDepth_NaN_ok() {
        Integer result = ArgumentsParser.ParseDepth("NaN");

        assertEquals(null, result);
    }

    @Test
    void validateDepth_zero_invalid() {
        boolean result = ArgumentsParser.ValidateDepth(0);

        assertFalse(result);
    }

    @Test
    void validateDepth_one_valid() {
        boolean result = ArgumentsParser.ValidateDepth(1);

        assertTrue(result);
    }

    @Test
    void validateLanguage_notPresent_returnsFalse() {
        Translator translator = mock(Translator.class);
        when(translator.IsSupportedTargetLanguageCode(any())).thenReturn(false);

        boolean result = ArgumentsParser.ValidateLanguage(translator, "EN");

        assertFalse(result);
    }

    @Test
    void validateLanguage_isPresent_returnsTrue() {
        Translator translator = mock(Translator.class);
        when(translator.IsSupportedTargetLanguageCode(any())).thenReturn(true);

        boolean result = ArgumentsParser.ValidateLanguage(translator, "EN");

        assertTrue(result);
    }

    @Test
    void validateUrl_documentPresent_returnsTrue() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(any())).thenReturn(new JsoupWebDocument(new Document("")));
        boolean result = ArgumentsParser.ValidateUrl(documentParser, "URL");

        assertTrue(result);
    }

    @Test
    void validateUrl_documentNull_returnsFalse() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(any())).thenReturn(null);
        boolean result = ArgumentsParser.ValidateUrl(documentParser, "URL");

        assertFalse(result);
    }

    @Test
    void validateUrls_validUrls_returnsTrue() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(any())).thenReturn(new JsoupWebDocument(new Document("")));
        boolean result = ArgumentsParser.ValidateUrls(documentParser, new String[] {"URL1", "URL2", "URL3"});

        assertTrue(result);
    }

    @Test
    void validateUrls_invalidUrls_returnsFalse() throws IOException {
        DocumentParser documentParser = mock(DocumentParser.class);
        when(documentParser.ParseUrl(any())).thenReturn(null);
        boolean result = ArgumentsParser.ValidateUrls(documentParser, new String[] {"URL1", "URL2", "URL3"});

        assertFalse(result);
    }
}