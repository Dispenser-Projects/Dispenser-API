package fr.theogiraudet.dispenser_api.dto;

import fr.theogiraudet.dispenser_api.dao.MinecraftResourceRepository;
import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import fr.theogiraudet.dispenser_api.domain.VersionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A DTO for {@link fr.theogiraudet.dispenser_api.domain.VersionInformation}
 */
@Data
@Schema(name = "Version Information", description = "Information about a specific version of Minecraft")
public class VersionInformationDto {

    /** The id of the version (i.e. version name) */
    @Schema(description = "The ID of the version (i.e. version name, example: 1.18.1)")
    private String id;

    /** The type of version */
    @Schema(description = "The type of the version")
    private VersionType type;

    /** The URL to the jar of the version */
    @Schema(description = "The URL to the jar of the version")
    private String downloadUrl;

    /** The hash of the version */
    @Schema(description = "The hash of the version")
    private String hash;

    /** The last time the version was updated */
    @Schema(description = "The last time the version was updated")
    private OffsetDateTime updateTime;

    /** The release date of the version */
    @Schema(description = "The release date of the version")
    private OffsetDateTime releaseTime;

    /** An URI map where keys are all accepted resource types for this version and values are the URL to the resource type */
    @Schema(description = "An object where the key is a disponible resource for this version and the value, the URL to access to the resource")
    private Map<String, String> resources;

    /**
     * @param id the id of the version (i.e. version name)
     * @param type the type of version
     * @param downloadUrl the URL to the jar of the version
     * @param hash the hash of the version
     * @param updateTime the last time updated of the version
     * @param releaseTime the release time of the version
     * @param repository the repository used to find all accepted resource type for this version
     */
    public VersionInformationDto(String id, VersionType type, String downloadUrl, String hash, OffsetDateTime updateTime, OffsetDateTime releaseTime, MinecraftResourceRepository repository) {
        this.id = id;
        this.type = type;
        this.downloadUrl = downloadUrl;
        this.hash = hash;
        this.updateTime = updateTime;
        this.releaseTime = releaseTime;

        resources = new HashMap<>();

        for(var asset: MinecraftAsset.values())
            if(repository.isPresentInVersion(asset, this.id))
                resources.put(asset.getId(), asset.getAllUri(id));
    }
}
