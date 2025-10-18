package ru.nsu.romanenko.local;

import java.util.Locale;

public abstract class Localization {
    private static Localization instance;

    public static void setLocale(Locale locale) {
        switch (locale.getLanguage()) {
            case "en":
                instance = new EnglishLocalization();
                break;
            case "ru":
                instance = new RussianLocalization();
                break;
            default:
                instance = new EnglishLocalization();
        }
    }

    public static String get(String key) {
        return instance.getString(key);
    }

    protected abstract String getString(String key);
}