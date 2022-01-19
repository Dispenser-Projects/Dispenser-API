package fr.theogiraudet.dispenser_api.dto.reduced_resource;

import fr.theogiraudet.dispenser_api.web.rest.VersionController;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Reduced version of {@link fr.theogiraudet.dispenser_api.domain.VersionInformation} to be displayed in JSON list instead of the complete version
 */
@Schema(name = "Reduced Version", description = "A summarised version of the Minecraft version")
@Data
public class ReducedVersion {

    /** The ID of the asset */
    private String id;
    /**
     * The URL to get the asset */
    private String url;

    /**
     * Create a new ReducedVersion
     * @param id the ID of the version
     */
    public ReducedVersion(String id) {
        this.id = id;
        this.url = linkTo(methodOn(VersionController.class).getVersion(id)).toUri().toString();
    }
}
