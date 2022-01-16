package fr.theogiraudet.minecraft_data.web.rest.abstract_resources;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * REST Controller for all Assets returning an InputStreamResource
 */
public abstract class ResourceController extends AssetController<InputStreamResource> {

    /**
     * Create a new Minecraft InputStreamResource Controller with the specified asset type
     * @param assetType the asset type
     */
    protected ResourceController(MinecraftAsset assetType) {
        super(assetType);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseEntity<InputStreamResource> getAsset(String assetName, String version, MediaType mimeType) {
        final var hashedAsset = repository.getHashedAsset(assetType, version, assetName);
        if(hashedAsset.isEmpty())
            return ResponseEntity.notFound().build();

        final var path = folder + File.separatorChar
                + MinecraftAsset.BLOCK_TEXTURE.getId()
                + File.separatorChar + hashedAsset.get().getId()
                + "." + MinecraftAsset.BLOCK_TEXTURE.getExtension();

        final var file = new File(path);

        try {
            return ResponseEntity.ok()
                    .contentLength((file).length())
                    .contentType(mimeType)
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
