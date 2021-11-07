package fr.theogiraudet.tp.spring.rest;

import fr.theogiraudet.tp.spring.dao.TextureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
//TODO remove
@CrossOrigin
public class TextureResource {

    @Autowired
    private TextureDao dao;

    @GetMapping(value = "/texture/block/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> getModel(@PathVariable("name") String name) {
        return dao.getTexture(name).map(ResponseEntity.ok()::body).orElse(ResponseEntity.notFound().build());
    }

}
