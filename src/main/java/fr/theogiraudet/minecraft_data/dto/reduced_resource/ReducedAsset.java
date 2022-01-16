package fr.theogiraudet.minecraft_data.dto.reduced_resource;

import fr.theogiraudet.minecraft_data.web.rest.abstract_resources.AssetController;
import lombok.Data;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Reduced version of {@link fr.theogiraudet.minecraft_data.domain.HashedAsset} to be displayed in JSON list instead of the complete version
 */
@Data
public class ReducedAsset {

    /** The ID of the asset */
    private String id;
    /**
     * The URL to get the asset */
    private String url;

    /**
     * Create a new ReducedAsset
     * @param id the ID of the asset
     * @param version the version of the asset
     * @param controllerClass the REST controller class, endpoint of this asset
     */
    public ReducedAsset(String id, String version, Class<? extends AssetController> controllerClass) {
        this.id = id;
        this.url = linkTo(methodOn(controllerClass).getAsset(id, version)).toUri().toString();
    }
}
