package fr.theogiraudet.minecraft_data.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.theogiraudet.minecraft_data.dao.MinecraftVersionRepository;
import fr.theogiraudet.minecraft_data.domain.Manifest;
import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Loader for Minecraft versions
 */
@Component
public class VersionLoader {

    /**
     * The URL of the Minecraft version manifest
     */
    @Value("${minecraft-data.manifest-url}")
    private String url;

    /**
     * The repository to used to push and update Minecraft versions
     */
    private final MinecraftVersionRepository repository;

    private final Logger logger = LoggerFactory.getLogger(VersionLoader.class);

    @Autowired
    public VersionLoader(MinecraftVersionRepository repository) {
        this.repository = repository;
    }

    /**
     * Update the database with new or modified Minecraft versions
     *
     * @return a list of Tuple where the first element is a modified/added version and the second is
     * the asset types to download for this version
     */
    public List<Tuple2<VersionInformation, List<MinecraftAsset>>> update() {
        logger.debug("Loading version manifest...");
        final var startingTime = System.currentTimeMillis();
        try {
            final var objectMapper = new ObjectMapper();
            final Manifest manifest = objectMapper.findAndRegisterModules().readValue(new URL(url), Manifest.class);
            var result = manifest.getVersions().parallelStream()
                    .map(this::updateUrl)
                    .map(this::manageUpdated)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            logger.debug("Version manifest loaded in {} ms", System.currentTimeMillis() - startingTime);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    /**
     * Push the version in the database if this one has be updated/added
     *
     * @return The current version in database and the list of asset types to download for this version if the version need
     * this assets to be updated, {@link Optional#empty()} otherwise
     */
    private Optional<Tuple2<VersionInformation, List<MinecraftAsset>>> manageUpdated(VersionInformation information) {
        final var old = repository.getVersion(information.getId());
        final var assetTypes = new ArrayList<>(MinecraftAsset.getAll());
        // The version doesn't exist in database or the existing version has a different hash -> update the version
        if (old.isEmpty() || !old.get().getHash().equals(information.getHash())) {
            repository.upsert(information);
            return Optional.of(Tuples.of(information, assetTypes));
        }
        // The version exists in database but all asset types haven't been dowload for this version -> need to download them
        if (!old.get().getLoadedAssets().containsAll(assetTypes)) {
            assetTypes.removeAll(old.get().getLoadedAssets());
            return Optional.of(Tuples.of(old.get(), assetTypes));
        }
        return Optional.empty();
    }

    /**
     * Update {@link VersionInformation#getUrl()} to the download URL of the jar
     *
     * @param information the {@link VersionInformation} whose URL needs to be updated
     * @return the same version with the URL updated
     */
    private VersionInformation updateUrl(VersionInformation information) {
        try {
            information.setUrl(JsonPath.read(new URL(information.getUrl()).openStream(), "$.downloads.client.url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return information;
    }
}
