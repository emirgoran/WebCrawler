import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class ArgumentsParser {

    private static final Set<String> LANGUAGES =
            Set.of("english", "german", "spanish");

    public static String GetSupportedLanguagesString()
    {
        return String.join(", ", ArgumentsParser.LANGUAGES);
    }

    public static boolean IsSupportedLanguage(String language)
    {
        if (language == null || language.length() == 0) {
            return false;
        }

        return LANGUAGES.contains(language.toLowerCase(Locale.ROOT));
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
