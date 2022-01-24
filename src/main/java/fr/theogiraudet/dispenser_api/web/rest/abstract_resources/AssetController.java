package fr.theogiraudet.dispenser_api.web.rest.abstract_resources;

import fr.theogiraudet.dispenser_api.dao.MinecraftResourceRepository;
import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import fr.theogiraudet.dispenser_api.dto.CustomPage;
import fr.theogiraudet.dispenser_api.dto.reduced_resource.ReducedAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * REST Controller for all Assets
 * @param <T> the resource type to return
 */
public abstract class AssetController<T> {

    /** the folder where are stored assets */
    @Value("${minecraft-data.data-folder}")
    protected String folder;

    /** The repository where get resource information */
    protected MinecraftResourceRepository repository;

    /** The asset type for this Controller */
    protected final MinecraftAsset assetType;

    /**
     * Create a new Minecraft Controller with the specified asset type
     * @param assetType the asset type
     */
    protected AssetController(MinecraftAsset assetType) {
        this.assetType = assetType;
    }

    /**
     * Set the repository to use for this controller
     * @param repository the repository to use
     */
    @Autowired
    public void setRepository(MinecraftResourceRepository repository) {
        this.repository = repository;
    }

    /**
     * @param pageable a pageable to apply to the query
     * @param version the version from which to get assets
     * @return status {@link org.springframework.http.HttpStatus#OK} with a {@link CustomPage} of {@link ReducedAsset} corresponding
     * to pageable if found, {@link org.springframework.http.HttpStatus#NOT_FOUND} otherwise
     */
    public ResponseEntity<CustomPage<ReducedAsset>> getAll(Pageable pageable, String version) {
        if(!repository.isPresentInVersion(assetType, version))
            return ResponseEntity.notFound().build();

        final var result = repository.getAllAssets(assetType, version, pageable)
                .flatMap(hashedAsset -> hashedAsset
                        .getVersionedAssets()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().contains(version))
                        .map(Map.Entry::getKey)
                        .map(res -> new ReducedAsset(res, version, this.getClass()))
                ).toList();

        final var page = PageableExecutionUtils
                .getPage(result.stream().skip((long) pageable.getPageNumber() * pageable.getPageSize()).limit(pageable.getPageSize()).toList(), pageable, result::size);
        final var customPage =
                CustomPage
                .from(page, getClass(), resource -> resource.getAll(null, version));
        return ResponseEntity.ok(customPage);
    }


    /**
     *
     * @param assetName the name of the asset to get
     * @param version the version from which to get the asset
     * @return status {@link org.springframework.http.HttpStatus#OK} with the resource corresponding to the asset if found,
     * {@link org.springframework.http.HttpStatus#NOT_FOUND} otherwise
     */
    public abstract ResponseEntity<T> getAsset(String assetName, String version);

}
