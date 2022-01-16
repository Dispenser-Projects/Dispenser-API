package fr.theogiraudet.minecraft_data.web.rest;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import fr.theogiraudet.minecraft_data.dto.CustomPage;
import fr.theogiraudet.minecraft_data.dto.reduced_resource.ReducedAsset;
import fr.theogiraudet.minecraft_data.web.rest.abstract_resources.JsonController;
import fr.theogiraudet.minecraft_data.web.swagger.CustomPageableAsQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for MCMETA files
 */
@RestController
@Tag(name = "Block Texture MCMETA")
public class BlockMcMetaController extends JsonController {

    /**
     * Create a new Minecraft MCMETA Controller
     */
    protected BlockMcMetaController() {
        super(MinecraftAsset.BLOCK_TEXTURE_MCMETA);
    }

    @CustomPageableAsQueryParam
    @Operation(summary = "Get a page of the list of all Block Texture MCMETA files for a specified version", operationId = "get_all_block_mcmeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCMETA files found an returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomPage.class))),
            @ApiResponse(responseCode = "404", description = "Specified version doesn't exist or doesn't have this resource type", content = @Content)
    })
    @GetMapping(path = "/{version}/block/mcmetas", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomPage<ReducedAsset>> getAll(@ParameterObject Pageable pageable,
                                                           @Parameter(description = "Version from which to get the list")
                                                           @PathVariable String version) {
        return super.getAll(pageable, version);
    }

    @Operation(summary = "Get a specific Block Texture MCMETA file for a specified version", operationId = "get_block_mcmeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCMETA file found an returned",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Specified version doesn't exist or doesn't have this resource", content = @Content)
    })
    @GetMapping(path = "/{version}/block/mcmeta/{assetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> getAsset(@Parameter(description = "MCMETA file name to get") @PathVariable String assetName,
                                           @Parameter(description = "Version from which to get the mcmeta") @PathVariable String version) {
        return super.getAsset(assetName, version);
    }

}
