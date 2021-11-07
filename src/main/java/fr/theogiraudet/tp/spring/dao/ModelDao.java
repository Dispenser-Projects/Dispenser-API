package fr.theogiraudet.tp.spring.dao;

import fr.theogiraudet.tp.spring.dto.Model;
import fr.theogiraudet.tp.spring.utils.Utils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ModelDao {

    private Map<String, Pair<Path, Model>> models;

    public ModelDao() {
        load();
    }

    public void load() {
        try {
            models = Files.list(Path.of("Minecraft/models/block"))
                    .map(path -> Pair.of(path, new Model(Utils.pathToFilename(path), "/model/" + Utils.pathToFilename(path))))
                    .collect(Collectors.toMap(pair -> pair.getSecond().getModelName(), Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Model> getModels() {
        return models.values().stream().map(Pair::getSecond).sorted(Comparator.comparing(Model::getModelName)).collect(Collectors.toList());
    }

    public Optional<String> getModel(String model) {
        return Optional.ofNullable(models.get(model))
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

}
