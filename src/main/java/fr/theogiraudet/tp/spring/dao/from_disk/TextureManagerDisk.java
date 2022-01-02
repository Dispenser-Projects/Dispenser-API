package fr.theogiraudet.tp.spring.dao.from_disk;

import fr.theogiraudet.tp.spring.utils.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TextureManagerDisk {

    private static final String PATH = "textures/block";

    private Map<String, Path> textures = new HashMap<>();

    public TextureManagerDisk(String folder) {
        load(folder);
    }

    private void load(String folder) {
        try {
            textures = Files.list(Path.of(folder + File.separatorChar + PATH))
                    .collect(Collectors.toMap(Utils::pathToFilename, Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
