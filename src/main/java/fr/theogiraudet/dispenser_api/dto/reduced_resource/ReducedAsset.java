package fr.theogiraudet.dispenser_api.dto.reduced_resource;

import fr.theogiraudet.dispenser_api.web.rest.abstract_resources.AssetController;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Reduced version of {@link fr.theogiraudet.dispenser_api.domain.HashedAsset} to be displayed in JSON list instead of the complete version
 */
@Schema(name = "Reduced Asset", description = "A summarised version of the asset")
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
