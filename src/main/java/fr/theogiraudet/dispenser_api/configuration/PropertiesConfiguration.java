package fr.theogiraudet.dispenser_api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minecraft-data")
@Data
public class PropertiesConfiguration {

    /**
     * The Minecraft Manifest URL where get Minecraft version URL
     */
    private String manifestUrl = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json";

    /**
     * The temporary folder where download and extract Minecraft version jar
     */
    private String tmpFolder = "tmp";

    /**
     * The folder where store extracted Minecraft resources
     */
    private String dataFolder = "data";

    /**
     * The check cycle for a new Minecraft version in the manifest
     */
    private String cronUpdate = "0 0 * * * ?";

}
