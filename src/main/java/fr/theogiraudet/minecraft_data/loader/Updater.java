package fr.theogiraudet.minecraft_data.loader;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import fr.theogiraudet.minecraft_data.domain.VersionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Component downloading regularly the Minecraft Manifest to check if a new Minecraft version is released
 */
@Component
@EnableScheduling
public class Updater {

    /** The version loader to load Minecraft version from the manifest */
    private final VersionLoader manifest;
    /** The asset loader */
    private final AssetLoader loader;

    /** lock to prevent the updater from being run several times at the same time */
    private boolean lock = false;

    private final Logger logger = LoggerFactory.getLogger(Updater.class);

    /**
     * Create a new Updater
     * @param manifest the version loader to load Minecraft version from the manifest
     * @param loader the asset loader
     */
    @Autowired
    public Updater(VersionLoader manifest, AssetLoader loader) {
        this.manifest = manifest;
        this.loader = loader;
    }

    /**
     * Function call regularly and at the start of the application to check if a new Minecraft version is released
     */
    @Scheduled(cron = "${minecraft-data.cron-update}")
    @EventListener(ApplicationReadyEvent.class)
    public void update() {
        if(!lock) {
            logger.debug("Start updating...");
            final var startTime = System.currentTimeMillis();
            lock = true;
            final var updated = manifest.update();
            updated.stream()
                    .filter(x -> x.getT1().getVersionType().equals(VersionType.RELEASE))
                    .sorted((x, x2) -> x2.getT1().compareTo(x.getT1()))
                    .filter(x -> x.getT1().getId().equals("1.0") || x.getT1().getId().equals("1.18.1"))
                    .forEach(version -> loader.loadVersion(version.getT1(), version.getT2().toArray(MinecraftAsset[]::new)));
            lock = false;
            logger.debug("Updated in {} ms", System.currentTimeMillis() - startTime);
        }
    }
}