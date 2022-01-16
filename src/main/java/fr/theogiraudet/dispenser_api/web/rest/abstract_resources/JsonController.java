package fr.theogiraudet.dispenser_api.web.rest.abstract_resources;

import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * REST Controller for all Assets returning JSON
 */
public abstract class JsonController extends AssetController<String> {

    /**
     * Create a new Minecraft JSON Controller with the specified asset type
     * @param assetType the asset type
     */
    protected JsonController(MinecraftAsset assetType) {
        super(assetType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> getAsset(String assetName, String version) {
        final var hashedAsset = repository.getHashedAsset(assetType, version, assetName);
        if (hashedAsset.isEmpty())
            return ResponseEntity.notFound().build();

        final var path = folder + File.separatorChar
                + assetType.getId()
                + File.separatorChar + hashedAsset.get().getId()
                + "." + assetType.getExtension();

        try {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(Files.lines(Path.of(path)).collect(Collectors.joining("\n")));
        } catch(IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}
