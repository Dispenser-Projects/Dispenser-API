package fr.theogiraudet.dispenser_api.dao;

import fr.theogiraudet.dispenser_api.domain.HashedAsset;
import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface to define operations for Minecraft Resource Repository
 */
public interface MinecraftResourceRepository {

    /**
     * @param assetType an asset type to get
     * @param version the version from which the assets are to be retrieved
     * @param pageable a {@link Pageable} to apply at the query
     * @return a {@link Page} of {@link HashedAsset} with the specified version and asset type
     */
    Page<HashedAsset> getAllAssets(MinecraftAsset assetType, String version, Pageable pageable);

    /**
     * @param assetType the type of the asset to test
     * @param hash a hash to test
     * @return true if a data of the specified type exists with the specified hash, false otherwise
     */
    boolean hashExists(MinecraftAsset assetType, String hash);

    /**
     * Add a new HashedAsset to the database
     * @param assetType the type of the asset
     * @param version the version of the asset
     * @param assetId the ID of the asset
     * @param hash the hash of the asset
     * @throws IllegalStateException if the asset with the same hash already exists in the database
     */
    void addNewAssetHash(MinecraftAsset assetType, String version, String assetId, String hash);

    /**
     * Add a new version and a new ID to an existing HashedAsset
     * If the version or the ID already exist for this HashedAsset, do nothing for the existing
     * @param assetType the type of the existing asset
     * @param hash the hash of the existing asset
     * @param version the new version to add to the asset
     * @param assetId the new ID to add to the asset
     * @throws IllegalStateException if the asset doesn't exist
     */
    void addToExistingHash(MinecraftAsset assetType, String hash, String version, String assetId);

    /**
     * @param assetType the type of the asset to get
     * @param version the version of the asset to get
     * @param assetId the ID of the asset to get
     * @return the HashedAsset corresponding at the 3 parameters if exists, {@link Optional#empty()} otherwise
     */
    Optional<HashedAsset> getHashedAsset(MinecraftAsset assetType, String version, String assetId);

    /**
     * @param assetType the type to test
     * @param version the version to test
     * @return true if the assetType exists for the specified version, false otherwise
     */
    boolean isPresentInVersion(MinecraftAsset assetType, String version);
}
