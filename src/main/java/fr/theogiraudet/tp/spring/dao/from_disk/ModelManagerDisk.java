package fr.theogiraudet.tp.spring.dao.from_disk;

import fr.theogiraudet.tp.spring.dto.Model;
import fr.theogiraudet.tp.spring.utils.Utils;
import org.springframework.data.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModelManagerDisk {

    private static final String PATH = "models/block";

    private Map<String, Pair<Path, Model>> models;

    public ModelManagerDisk(String folder) {
        load(folder);
    }

    private void load(String folder) {
        try {
            models = Files.list(Path.of(folder + File.separatorChar + PATH))
                    .map(path -> Pair.of(path, new Model(Utils.pathToFilename(path), "/model/" + Utils.pathToFilename(path))))
                    .collect(Collectors.toMap(pair -> pair.getSecond().getModelName(), Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Model> getModels() {
        return models.values().stream().map(Pair::getSecond).sorted(Comparator.comparing(Model::getModelName)).collect(Collectors.toList());
    }

}
