package fr.theogiraudet.minecraft_data.dao.mongo;

import fr.theogiraudet.minecraft_data.dao.MinecraftResourceRepository;
import fr.theogiraudet.minecraft_data.domain.HashedAsset;
import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Implementation of MinecraftResourceRepository for MongoDB
 */
@Transactional
@Repository
public class MinecraftResourceRepositoryMongo implements MinecraftResourceRepository {

    private static final String ASSET_TYPE_FIELD = "asset_type";
    private static final String VERSIONS_FIELD = "versions";
    private static final String VERSIONED_ASSETS_FIELD = "versionedAssets";

    private final MongoTemplate mongoTemplate;

    /**
     * Create a new {@link MinecraftResourceRepository}
     *
     * @param template the MongoTemplate to use in this repository
     */
    public MinecraftResourceRepositoryMongo(MongoTemplate template) {
        this.mongoTemplate = template;
    }

    /**
     * @param assetType an asset type to get
     * @param version the version from which the assets are to be retrieved
     * @param pageable a {@link Pageable} to apply at the query
     * @return a {@link Page} of {@link HashedAsset} with the specified version and asset type
     */
    @Override
    public Page<HashedAsset> getAllAssets(MinecraftAsset assetType, String version, Pageable pageable) {
        final var query = new Query()
                .addCriteria(where(ASSET_TYPE_FIELD).is(assetType))
                .addCriteria(where(VERSIONS_FIELD).is(version))
                .with(pageable);

        final var list = mongoTemplate.find(query, HashedAsset.class);
        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), HashedAsset.class));
    }

    /**
     * @param assetType the type of the asset to test
     * @param hash a hash to test
     * @return true if a data of the specified type exists with the specified hash, false otherwise
     */
    @Override
    public boolean hashExists(MinecraftAsset assetType, String hash) {
        final var query = new Query()
                .addCriteria(where(ASSET_TYPE_FIELD).is(assetType))
                .addCriteria(where("_id").is(hash));

        return !mongoTemplate.find(query, HashedAsset.class).isEmpty();
    }

    /**
     * Add a new HashedAsset to the database
     * @param assetType the type of the asset
     * @param version the version of the asset
     * @param assetId the ID of the asset
     * @param hash the hash of the asset
     * @throws IllegalStateException if the asset with the same hash already exists in the database
     */
    @Override
    public void addNewAssetHash(MinecraftAsset assetType, String version, String assetId, String hash) {
        if (hashExists(assetType, hash))
            throw new IllegalStateException("Hash already exists in the database");
        else
            try {
                mongoTemplate.insert(new HashedAsset(hash, assetType, Map.of(assetId, List.of(version)), Set.of(version)));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Add a new version and a new ID to an existing HashedAsset
     * If the version or the ID already exist for this HashedAsset, do nothing for the existing
     * @param assetType the type of the existing asset
     * @param hash the hash of the existing asset
     * @param version the new version to add to the asset
     * @param assetId the new ID to add to the asset
     * @throws IllegalStateException if the asset doesn't exist
     */
    @Override
    public void addToExistingHash(MinecraftAsset assetType, String hash, String version, String assetId) {
        final var query = new Query()
                .addCriteria(where(ASSET_TYPE_FIELD).is(assetType))
                .addCriteria(where("_id").is(hash));

        final var result = mongoTemplate.findOne(query, HashedAsset.class);

        if (result != null) {
            final var versions = result.getVersionedAssets().getOrDefault(assetId, new LinkedList<>());
            versions.add(version);
            result.getVersionedAssets().put(assetId, versions);
            result.getVersions().add(version);
            mongoTemplate.save(result);
        } else
            throw new IllegalStateException("Hash doesn't exist in the database");
    }

    /**
     * @param assetType the type of the asset to get
     * @param version the version of the asset to get
     * @param assetId the ID of the asset to get
     * @return the HashedAsset corresponding at the 3 parameters if exists, {@link Optional#empty()} otherwise
     */
    @Override
    public Optional<HashedAsset> getHashedAsset(MinecraftAsset assetType, String version, String assetId) {
        final var query = new Query()
                .addCriteria(where(ASSET_TYPE_FIELD).is(assetType))
                .addCriteria(where(VERSIONED_ASSETS_FIELD + "." + assetId).exists(true))
                .addCriteria(where(VERSIONS_FIELD).is(version))
                .limit(1);

        return Optional.ofNullable(mongoTemplate.findOne(query, HashedAsset.class));
    }

    /**
     * @param assetType the type to test
     * @param version the version to test
     * @return true if the assetType exists for the specified version, false otherwise
     */
    @Override
    public boolean isPresentInVersion(MinecraftAsset assetType, String version) {
        final var query = new Query()
                .addCriteria(where(ASSET_TYPE_FIELD).is(assetType))
                .addCriteria(where(VERSIONS_FIELD).is(version));

        return mongoTemplate.exists(query, HashedAsset.class);
    }
}
