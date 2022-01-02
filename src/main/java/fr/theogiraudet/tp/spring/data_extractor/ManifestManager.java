package fr.theogiraudet.tp.spring.data_extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.theogiraudet.tp.spring.domain.Manifest;
import fr.theogiraudet.tp.spring.domain.VersionInformation;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ManifestManager {

    @Value("${minecraft-manifest-url}")
    private String url;
    private Map<String, VersionInformation> version = new HashMap<>();

    public void update() {
        try {
            final var objectMapper = new ObjectMapper();
            final Manifest manifest = objectMapper.findAndRegisterModules().readValue(new URL(url), Manifest.class);
            manifest.getVersions().stream().map(this::updateUrl).forEach((info) -> version.put(info.getId(), info));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VersionInformation updateUrl(VersionInformation information) {
        try {
        information.setUrl(JsonPath.read(new URL(information.getUrl()).openStream(), "$.downloads.client.url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return information;
    }

    public Optional<VersionInformation> getInformations(String version) {
        return Optional.ofNullable(this.version.get(version));
    }

    public Map<String, VersionInformation> getVersions() {
        return Collections.unmodifiableMap(version);
    }
}
