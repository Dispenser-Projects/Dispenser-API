package fr.theogiraudet.tp.spring.rest;

import fr.theogiraudet.tp.spring.dao.from_disk.TextureManagerDisk;
import fr.theogiraudet.tp.spring.dao.from_disk.VersionLoaderDisk;
import fr.theogiraudet.tp.spring.data_extractor.ManifestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;

import javax.annotation.PostConstruct;

@RestController
@Controller
//TODO remove
@CrossOrigin
public class TextureResource {

    private final Logger logger = LoggerFactory.getLogger(TextureResource.class);

    @Autowired
    private ManifestManager manager;

    @Autowired
    private VersionLoaderDisk loader;

    @PostConstruct
    private void initialize() {
        logger.info("Loading version manifest...");
        final var startingTime = System.currentTimeMillis();
        manager.update();
        logger.info("Version manifest loaded in {} ms", System.currentTimeMillis() - startingTime);
    }


    @GetMapping(value = "/test", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Void> getModel() {
        final var version = "1.17.1";
        logger.info("Loading version {}...", version);
        final var startingTime = System.currentTimeMillis();
        loader.loadVersion(version);
        logger.info("Version {} loaded in {} ms", version, System.currentTimeMillis() - startingTime);
        return ResponseEntity.ok(null);
    }

    /*@Autowired
    private TextureManagerDisk dao;

    @GetMapping(value = "/texture/block/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> getModel(@PathVariable("name") String name) {
        return dao.getTexture(name).map(ResponseEntity.ok()::body).orElse(ResponseEntity.notFound().build());
    }*/

}
