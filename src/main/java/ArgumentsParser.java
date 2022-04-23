import Translation.TranslatorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArgumentsParser {

    public static boolean IsSupportedLanguageCode(String language)
    {
        if (language == null || language.length() == 0) {
            return false;
        }

        return TranslatorService.IsSupportedLanguageCode(language);
    }

    public static Integer ParseDepth(String depthString)
    {
        try {
            return Integer.parseInt(depthString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Document ParseUrl(String URL)
    {
        try {
            return Jsoup.connect(URL).get();
        } catch (Exception e) {
            return null;
        }
    }
}
