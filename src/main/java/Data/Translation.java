package Data;

public class Translation {
    private String originalText[];
    private String translatedText[];
    private String sourceLanguage;
    private String targetLanguage;

    public Translation(String originalText[], String[] translatedText, String sourceLanguage, String targetLanguage) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public String[] getOriginalText() {
        return originalText;
    }

    public String[] getTranslatedText() {
        return translatedText;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }
}
