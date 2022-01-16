package fr.theogiraudet.dispenser_api.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents informations got from official Minecraft Manifest concerning a Minecraft version
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document("versions")
@Data
public class VersionInformation implements Comparable<VersionInformation> {

    /** The id of the version (i.e. version name) */
    @Id
    private String id;

    /** The type of version */
    @Field("version_type")
    private VersionType versionType;

    /** The URL to the jar of the version */
    private String url;

    /** The hash of the version */
    @JsonAlias("sha1")
    private String hash;

    /** The last time updated of the version */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:m:ssXXX")
    private OffsetDateTime time;

    /** The release time of the version */
    @Field("release_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:m:ssXXX")
    private OffsetDateTime releaseTime;

    /** The list of all loaded assets for this version */
    @Field("loaded_assets")
    private List<MinecraftAsset> loadedAssets;

    public VersionInformation() {
        this.loadedAssets = new LinkedList<>();
    }

    /**
     * Set the ID of the version
     * @param id the ID
     */
    @JsonSetter
    public void setId(String id) {
        this.id = id;
        versionType = VersionType.toVersionType(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(VersionInformation o) {
        return releaseTime.compareTo(o.releaseTime);
    }

    public void addLoadedAsset(MinecraftAsset asset) {
        loadedAssets.add(asset);
    }
}
