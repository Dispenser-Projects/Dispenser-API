package fr.theogiraudet.minecraft_data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

/**
 * Class representing a Minecraft Hashed Asset (models, textures, sounds...)
 */
@Document(collection = "assets")
public @Data
class HashedAsset {

    /**
     * The id of the HashedAsset, i.e. the hash of the file of the asset
     */
    @Id
    @JsonAlias("_id")
    private String id;

    /**
     * The type of the asset (model, textures, sounds...)
     */
    @Field(value = "asset_type")
    private MinecraftAsset assetType;

    /**
     * The assets name (i.e. file name) corresponding to this asset file, map to versions where the asset name is present
     * For example some MCMETA files have exactly the same content, the HashedAsset is therefore the same and the original file names are stored in this map
     */
    private Map<String, List<String>> versionedAssets;

    /**
     * Versions using this asset
     */
    private Set<String> versions;

    /**
     * Create a new HashedAsset
     * @param id the id of the HashedAsset, i.e. the hash of the file of the asset
     * @param assetType the type of the asset (model, textures, sounds...)
     * @param versionedAssets the assets name (i.e. file name) corresponding to this asset file, map to versions where the asset name is present
     * For example some MCMETA files have exactly the same content, the HashedAsset is therefore the same and the original file names are stored in this map
     * @param versions versions using this asset
     */
    public HashedAsset(String id, MinecraftAsset assetType, Map<String, List<String>> versionedAssets, Set<String> versions) {
        this.id = id;
        this.assetType = assetType;
        this.versionedAssets = new HashMap<>(versionedAssets);
        this.versions = new HashSet<>(versions);
    }
}
