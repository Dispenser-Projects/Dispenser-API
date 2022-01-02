package fr.theogiraudet.tp.spring.dao.from_disk;

import fr.theogiraudet.tp.spring.dao.Version;
import fr.theogiraudet.tp.spring.domain.VersionInformation;
import fr.theogiraudet.tp.spring.dto.Model;
import org.apache.commons.io.FileUtils;
import org.springframework.data.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VersionDisk implements Version {

    private final String id;
    private final VersionInformation informations;
    private Map<String, Path> textures = new HashMap<>();
    private Map<String, Pair<Path, Model>> models;

    public VersionDisk(VersionInformation informations) {
        this.informations = informations;
        this.id = informations.getId();
    }

    @Override
    public Optional<String> getTexture(String textureName) {
        final var path = textures.get(textureName);
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

    @Override
    public Optional<String> getModel(String modelName) {
        return Optional.ofNullable(models.get(modelName))
                .map(Pair::getFirst)
                .flatMap(this::readString);
    }

    private Optional<String> readString(Path path) {
        try {
            return Optional.of(Files.readString(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public VersionInformation getInformations() {
        return informations;
    }

    @Override
    public String getId() {
        return id;
    }


}
