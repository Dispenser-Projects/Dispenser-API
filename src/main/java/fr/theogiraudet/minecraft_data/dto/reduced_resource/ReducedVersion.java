package fr.theogiraudet.minecraft_data.dto.reduced_resource;

import fr.theogiraudet.minecraft_data.web.rest.VersionController;
import lombok.Data;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Reduced version of {@link fr.theogiraudet.minecraft_data.domain.VersionInformation} to be displayed in JSON list instead of the complete version
 */
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
