package fr.theogiraudet.minecraft_data.domain;

import java.util.Arrays;

/**
 * Enumeration of Minecraft Version type
 */
public enum VersionType {

    RELEASE("\\d+(\\.\\d+){1,2}"),
    RELEASE_CANDIDATE("\\d+(\\.\\d+){1,2}-rc\\d+"),
    PRE_RELEASE("\\d+(\\.\\d+){1,2}-pre\\d+"),
    SNAPSHOT("\\d+w\\d+\\w"),
    OTHER(".*");

    /** The regex a version has to match to be of this type */
    private final String regex;

    /**
     * @param regex the regex a version has to match to be of this type
     */
    VersionType(String regex) {
        this.regex = regex;
    }

    /**
     * @param version the version ID
     * @return the version type according to its ID
     */
    public static VersionType toVersionType(String version) {
        return Arrays.stream(VersionType.values()).filter(x -> version.matches(x.regex)).findFirst().orElse(null);
    }
}
