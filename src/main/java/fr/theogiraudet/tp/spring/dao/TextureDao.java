package fr.theogiraudet.tp.spring.dao;

import fr.theogiraudet.tp.spring.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

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


@Component
public class TextureDao {

    private Map<String, Path> textures = new HashMap<>();

    public TextureDao() {
        load();
    }

    public void load() {
        try {
            textures = Files.list(Path.of("Minecraft/textures/block"))
                    .collect(Collectors.toMap(Utils::pathToFilename, Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> getTexture(String imageName) {
        final var path = textures.get(imageName);
        if (path == null)
            return Optional.empty();

        final byte[] fileContent;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(path.toString()));
            final var encodedString = Base64.getEncoder().encodeToString(fileContent);
            return Optional.of(encodedString);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
