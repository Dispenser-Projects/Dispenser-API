package fr.theogiraudet.tp.spring.utils;

import java.nio.file.Path;

public class Utils {

    public static String pathToFilename(Path path) {
        return path.getFileName().toString().replaceFirst("[.][^.]+$", "");
    }

}
