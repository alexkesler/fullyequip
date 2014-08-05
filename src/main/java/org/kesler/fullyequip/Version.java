package org.kesler.fullyequip;

/**
 * Класс для хранения версии приложения
 */
public class Version {

    private static String version = "1.0.0";
    private static String releaseDate = "05.08.2014";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
