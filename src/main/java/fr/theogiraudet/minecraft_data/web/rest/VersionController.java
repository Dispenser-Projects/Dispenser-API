package fr.theogiraudet.minecraft_data.web.rest;

import fr.theogiraudet.minecraft_data.dao.MinecraftVersionRepository;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import fr.theogiraudet.minecraft_data.domain.VersionType;
import fr.theogiraudet.minecraft_data.dto.CustomPage;
import fr.theogiraudet.minecraft_data.dto.VersionInformationDto;
import fr.theogiraudet.minecraft_data.dto.mapper.VersionInformationMapper;
import fr.theogiraudet.minecraft_data.dto.reduced_resource.ReducedVersion;
import fr.theogiraudet.minecraft_data.web.swagger.CustomPageableAsQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST Controller for Minecraft Versions
 */
@RestController
@Tag(name = "Version")
public class VersionController {

    /**
     * The repository where get version information
     */
    private final MinecraftVersionRepository repository;

    /**
     * The mapper to convert {@link VersionInformation} to {@link ReducedVersion}
     */
    private final VersionInformationMapper mapper;

    /**
     * Create a new Minecraft Version Controller
     *
     * @param repository the repository to use to get version information
     * @param mapper     the mapper to use to convert {@link VersionInformation} to {@link ReducedVersion}
     */
    @Autowired
    public VersionController(MinecraftVersionRepository repository, VersionInformationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @CustomPageableAsQueryParam
    @Operation(summary = "Get a page of the list of all Minecraft versions", operationId = "get_all_version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versions found an returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomPage.class)))
    })
    @GetMapping(path = "/versions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomPage<ReducedVersion>> getVersions(
            @ParameterObject
            @PageableDefault(sort = {"releaseTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "Version type to filter the list") @RequestParam(required = false) VersionType type) {
        final var example = getExample(type);

        final var page = example == null ? repository.findAll(pageable) : repository.findAll(example, pageable);
        final var pageMapped = page.map(version -> new ReducedVersion(version.getId()));
        final var customPage = CustomPage
                .from(pageMapped, VersionController.class, ressource -> ressource.getVersions(null, null));

        return ResponseEntity.ok(customPage);
    }

    @Operation(summary = "Get information about the latest Minecraft Version", operationId = "get_latest_version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about latest version found an returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionInformationDto.class))),
    })
    @GetMapping(path = "/version/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionInformationDto> getLatest(
            @Parameter(description = "Version type of the latest version wanted") @RequestParam(defaultValue = "RELEASE") VersionType type) {
        return ResponseEntity.ok(mapper.versionToDto(repository.getLatestVersion(type)));
    }

    @Operation(summary = "Get information about a specific Minecraft Version", operationId = "get_version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about specified version found an returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionInformationDto.class))),
            @ApiResponse(responseCode = "404", description = "Specified version doesn't exist", content = @Content)
    })
    @GetMapping(path = "/version/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionInformationDto> getVersion(
            @Parameter(description = "Version wanted") @PathVariable String version) {
        return repository.getVersion(version)
                .map(mapper::versionToDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }


    /**
     * @param type the version type to use for the example
     * @return an {@link Example} with the specified type
     */
    private Example<VersionInformation> getExample(VersionType type) {
        if (type == null)
            return null;
        final var version = new VersionInformation();
        version.setVersionType(type);
        return Example.of(version);
    }
}
