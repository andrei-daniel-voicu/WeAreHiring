public class Language {
    private final String language;
    private final String level;

    public Language(String language, String level) {
        this.language = language;
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public String getLevel() {
        return level;
    }

    public String toString() {
        return language + " - " + level;
    }
}