package Translation;

import Exceptions.TranslationInvalidArgumentException;
import Exceptions.TranslationNotSuccessfulException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TranslatorServiceTest {

    @Test
    void translateText() throws TranslationInvalidArgumentException, TranslationNotSuccessfulException {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        String[] originalText = new String[] {"Translation"};
        String[] expectedTranslationText = new String[] {"Translation"};

        Translation translation = translatorService.TranslateText(originalText, "EN");

        Assertions.assertEquals("English", translation.getSourceLanguage());
        Assertions.assertEquals("English", translation.getTargetLanguage());
        Assertions.assertEquals(1, translation.getTranslatedText().length);
        Assertions.assertEquals(expectedTranslationText[0], translation.getTranslatedText()[0]);
    }

    @Test
    void isSupportedTargetLanguageCode_EN_isSupported() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        boolean isSupportedTargetLanguageCode = translatorService.IsSupportedTargetLanguageCode("EN");

        Assertions.assertTrue(isSupportedTargetLanguageCode);
    }

    @Test
    void isSupportedTargetLanguageCode_DE_isSupported() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        boolean isSupportedTargetLanguageCode = translatorService.IsSupportedTargetLanguageCode("DE");

        Assertions.assertTrue(isSupportedTargetLanguageCode);
    }

    @Test
    void isSupportedTargetLanguageCode_IT_isSupported() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        boolean isSupportedTargetLanguageCode = translatorService.IsSupportedTargetLanguageCode("IT");

        Assertions.assertTrue(isSupportedTargetLanguageCode);
    }

    @Test
    void isSupportedTargetLanguageCode_SP_isNotSupported() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        boolean isSupportedTargetLanguageCode = translatorService.IsSupportedTargetLanguageCode("SP");

        Assertions.assertFalse(isSupportedTargetLanguageCode);
    }

    @Test
    void getTargetLanguageNameByLanguageCode() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        String language = translatorService.GetTargetLanguageNameByLanguageCode("EN");

        Assertions.assertEquals("English", language);
    }

    @Test
    void getTargetLanguagesListFormatted() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        String result = translatorService.GetTargetLanguagesListFormatted();

        Assertions.assertTrue(result.contains("DE: German"));
        Assertions.assertTrue(result.contains("IT: Italian"));
        Assertions.assertTrue(result.contains("EN: English"));
        Assertions.assertEquals(3, result.split("\n").length);
    }

    @Test
    void getSourceLanguageNameByLanguageCode() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        String language = translatorService.GetSourceLanguageNameByLanguageCode("DE");

        Assertions.assertEquals("German", language);
    }

    @Test
    void getSourceLanguageFromTranslationJsonArray() {
        TranslatorService translatorService = new TranslatorService(getTestTranslatorApi());

        String detectedLanguage = translatorService.GetSourceLanguageFromTranslationJsonArray(getTranslationTestJsonArray());

        Assertions.assertEquals("English", detectedLanguage);
    }

    @Test
    void getLanguagesHashMap() {
        Document document = new Document("");
        document.append(getLanguagesTestJsonArray().toString());
        HashMap<String, String> languagesHashMap = TranslatorService.GetLanguagesHashMap(getLanguagesTestJsonArray());

        Assertions.assertEquals(3, languagesHashMap.size());
        Assertions.assertEquals("Italian", languagesHashMap.get("IT"));
        Assertions.assertEquals("German", languagesHashMap.get("DE"));
        Assertions.assertEquals("English", languagesHashMap.get("EN"));
    }

    @Test
    void convertLanguagesJsonArrayToHashMap() {
        JSONArray languagesJsonArr = getLanguagesTestJsonArray();
        HashMap<String, String> languagesHashMap = TranslatorService.ConvertLanguagesJsonArrayToHashMap(languagesJsonArr);

        Assertions.assertEquals(3, languagesHashMap.size());
        Assertions.assertEquals("Italian", languagesHashMap.get("IT"));
        Assertions.assertEquals("German", languagesHashMap.get("DE"));
        Assertions.assertEquals("English", languagesHashMap.get("EN"));
    }

    @Test
    void convertTranslationsJsonArrayToStringArray() {
        JSONArray translationsJsonArr = new JSONArray();
        for (int i = 0; i < 3; i++) {
            JSONObject translationJsonObj = new JSONObject();
            translationJsonObj.put("text", "Translation " + i);
            translationsJsonArr.put(translationJsonObj);
        }

        String[] result = TranslatorService.ConvertTranslationsJsonArrayToStringArray(translationsJsonArr);

        Assertions.assertEquals(3, result.length);
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals("Translation " + i, result[i]);
        }
    }

    private TranslatorApi getTestTranslatorApi() {
        TranslatorApi translatorApi = mock(TranslatorApi.class);

        when(translatorApi.GetLanguagesJsonArray(false))
                .thenReturn(getLanguagesTestJsonArray());

        when(translatorApi.GetLanguagesJsonArray(true))
                .thenReturn(getLanguagesTestJsonArray());

        when(translatorApi.GetTranslationsJsonArray(any(), anyString())).thenReturn(getTranslationTestJsonArray());

        return translatorApi;
    }

    private JSONArray getLanguagesTestJsonArray() {
        JSONArray languagesJsonArr = new JSONArray();

        JSONObject langJsonObj1 = new JSONObject();
        langJsonObj1.put("language", "EN");
        langJsonObj1.put("name", "English");

        JSONObject langJsonObj2 = new JSONObject();
        langJsonObj2.put("language", "DE");
        langJsonObj2.put("name", "German");

        JSONObject langJsonObj3 = new JSONObject();
        langJsonObj3.put("language", "IT");
        langJsonObj3.put("name", "Italian");

        languagesJsonArr.put(langJsonObj1);
        languagesJsonArr.put(langJsonObj2);
        languagesJsonArr.put(langJsonObj3);

        return languagesJsonArr;
    }

    private JSONArray getTranslationTestJsonArray() {
        JSONArray translationsJsonArr = new JSONArray();

        JSONObject translationJsonObj = new JSONObject();
        translationJsonObj.put("text", "Translation");
        translationJsonObj.put("detected_source_language", "EN");

        translationsJsonArr.put(translationJsonObj);

        return translationsJsonArr;
    }
}