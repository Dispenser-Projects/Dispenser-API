package fr.theogiraudet.dispenser_api.web.rest;

import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import fr.theogiraudet.dispenser_api.loader.TilesetBlockPosition;
import fr.theogiraudet.dispenser_api.web.rest.abstract_resources.AssetController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Block Tileset")
public class BlockTilesetController extends AssetController<InputStreamResource> {

    public BlockTilesetController() {
        super(MinecraftAsset.BLOCK_TILESET);
    }

    @Override
    public ResponseEntity<InputStreamResource> getAsset(String assetName, String version) {
        return ResponseEntity.internalServerError().build();
    }


    public ResponseEntity<InputStreamResource> getAsset(String assetName, String version, MediaType mimeType) {
        final var hashedAsset = repository.getHashedAsset(assetType, version, assetName);
        if(hashedAsset.isEmpty())
            return ResponseEntity.notFound().build();

        final var path = folder + File.separatorChar
                + MinecraftAsset.BLOCK_TILESET.getId()
                + File.separatorChar + hashedAsset.get().getId()
                + "." + MinecraftAsset.BLOCK_TILESET.getExtension();

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

    @Operation(summary = "Get a Block Tileset file for a specified version", operationId = "get_block_tileset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block Tileset file found an returned",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Specified version doesn't exist or doesn't have this resource", content = @Content)
    })
    @GetMapping(path = "/{version}/block/tileset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getTileset(
    @Parameter(description = "Version from which to get the texture") @PathVariable String version) {
        return getAsset("tileset", version, MediaType.IMAGE_PNG);
    }

    @Operation(summary = "Get Block Tileset position for a specified version", operationId = "get_block_tileset_position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block Tileset position found an returned",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Specified version doesn't exist or doesn't have this resource", content = @Content)
    })
    @GetMapping(path = "/{version}/block/tileset/{assetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPosition(
            @Parameter(description = "Block Tileset position name to get") @PathVariable String assetName,
            @Parameter(description = "Version from which to get the model") @PathVariable String version) {
        int[] position =  TilesetBlockPosition.getPosition(version, assetName);
  
        if(position != null) {
            String ret = String.format("[%d,%d,%d,%d]", position[0],position[1],position[2], position[3]);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(ret);
        }

        return ResponseEntity.noContent().build(); 
    }
}
