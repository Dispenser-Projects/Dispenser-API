package fr.theogiraudet.tp.spring.dao.from_disk;

import fr.theogiraudet.tp.spring.dao.Version;
import fr.theogiraudet.tp.spring.dao.VersionManager;
import fr.theogiraudet.tp.spring.data_extractor.ManifestManager;
import fr.theogiraudet.tp.spring.domain.VersionInformation;
import fr.theogiraudet.tp.spring.dto.Model;
import fr.theogiraudet.tp.spring.utils.Utils;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VersionLoaderDisk implements VersionLoader {

    private static final String MODEL_PATH = "models/block";
    private static final String TEXTURE_PATH = "textures/block";
    @Value("${minecraft-data-folder}")
    private final String folderPath = "data";
    private ManifestManager manager;

    private Map<String, Pair<Path, Model>> models;

    @Autowired
    public VersionLoaderDisk(ManifestManager manager) {
        this.manager = manager;
    }

    @Override
    public Version loadVersion(String id) {
        final var informations = manager.getInformations(id);
        if (informations.isPresent()) {
            try {
                URL website = new URL(informations.get().getUrl());
                final var path = Path.of(folderPath + File.separatorChar + id);
                deleteDirectoryStream(path);
                Files.createDirectories(path);
                final var file = path.toString() + File.separatorChar + id + ".jar";
                FileUtils.copyURLToFile(website, new File(file));
                try(final ZipFile archive = new ZipFile(file)) {
                    final var files = archive.getFileHeaders().stream().filter(a -> a.getFileName().startsWith("assets/minecraft")).collect(Collectors.toList());
                    for(var fheader: files)
                        archive.extractFile(fheader, path.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    void deleteDirectoryStream(Path path) throws IOException {
        if(new File(path.toString()).exists())
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(path);
    }

    @Override
    public void update(Map<String, Version> loadedVersion, Map<String, VersionInformation> existingVersions, VersionManager manager) {
        manager.update();
        final var updatedManifest = this.manager.getVersions();
        final var commonVersions = new HashSet<>(updatedManifest.keySet());
        commonVersions.retainAll(loadedVersion.keySet());
        for(var version: commonVersions) {
            //loadedVersion.get(version).getInformations().
        }
    }

    private void loadModel(String folder) {
        try {
            models = Files.list(Path.of(folder + File.separatorChar + MODEL_PATH))
                    .map(path -> Pair.of(path, new Model(Utils.pathToFilename(path), "/model/" + Utils.pathToFilename(path))))
                    .collect(Collectors.toMap(pair -> pair.getSecond().getModelName(), Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
