package fr.theogiraudet.minecraft_data;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utils class
 */
public class Utils {

    private Utils() {}

    /**
     * @param file a resource file
     * @return the content of the file if exists, {@link Optional#empty()} otherwise
     */
    public static Optional<String> readResourceFile(String file) {
        final var stream = Utils.class.getClassLoader().getResourceAsStream(file);
        return Optional.ofNullable(stream)
                .map(str -> new BufferedReader(new InputStreamReader(str))
                .lines()
                .collect(Collectors.joining("\n")));
    }

}
