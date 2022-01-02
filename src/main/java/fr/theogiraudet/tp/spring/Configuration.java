package fr.theogiraudet.tp.spring;

import fr.theogiraudet.tp.spring.data_extractor.ManifestManager;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public static ManifestManager getManifestManager() {
        return new ManifestManager();
    }

}
