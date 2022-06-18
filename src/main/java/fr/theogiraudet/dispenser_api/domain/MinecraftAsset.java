package fr.theogiraudet.dispenser_api.domain;

import fr.theogiraudet.dispenser_api.web.rest.BlockMcMetaController;
import fr.theogiraudet.dispenser_api.web.rest.BlockModelController;
import fr.theogiraudet.dispenser_api.web.rest.BlockTextureController;
import fr.theogiraudet.dispenser_api.web.rest.BlockStateController;
import fr.theogiraudet.dispenser_api.web.rest.abstract_resources.AssetController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Representing the supported Minecraft Asset by the API
 */
public enum MinecraftAsset implements MinecraftResource {

    /** Minecraft Texture Blocks */
    BLOCK_TEXTURE("texture.block", "assets/minecraft/textures/block", "png", BlockTextureController.class),

    /** Minecraft Texture MCMETA */
    BLOCK_TEXTURE_MCMETA("texture.block.mcmeta", "assets/minecraft/textures/block", "png.mcmeta", BlockMcMetaController.class),

    /** Minecraft Model Blocks */
    BLOCK_MODEL("model.block", "assets/minecraft/models/block", "json", BlockModelController.class),

    BLOCK_STATE("blockstate.block", "assets/minecraft/blockstates", "json", BlockStateController.class),

    BLOCK_TILESET("tileset.block",null,"png",BlockTilesetController.class);
    
    /** The id of the asset type */
    private final String id;

    /** The path to the asset folder in the version jar */
    private final String pathInJar;

    /** The file extension of the asset files */
    private final String extension;

    /** The REST controller of the asset type */
    private final Class<? extends AssetController<?>> controller;

    /**
     *
     * @param id the id of the asset type
     * @param pathInJar the path to the asset folder in the version jar
     * @param extension the file extension of the asset files
     * @param controller the REST controller of the asset type
     */
    MinecraftAsset(String id, String pathInJar, String extension, Class<? extends AssetController<?>> controller) {
        this.id = id;
        this.pathInJar = pathInJar;
        this.extension = extension;
        this.controller = controller;
    }

    /**
     * @return the id of the asset type
     */
    public String getId() {
        return id;
    }

    /**
     * @param version the version to use to build the URI
     * @return the URI to the function to get all assets for this type and for the specified version
     */
    @Override
    public String getAllUri(String version) {
        return linkTo(methodOn(controller).getAll(null, version)).toUri().toString();
    }

    /**
     * @return the path to the asset folder in the version jar
     */
    public String getPathInJar() {
        return pathInJar;
    }

    /**
     * @return the file extension of the asset files
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @return all MinecraftAsset to load in the application
     */
    public static List<MinecraftAsset> getAll() {
        return List.of(BLOCK_TEXTURE, BLOCK_TEXTURE_MCMETA, BLOCK_MODEL, BLOCK_STATE);
    }
}
