package fr.theogiraudet.tp.spring.rest;

import fr.theogiraudet.tp.spring.dao.ModelDao;
import fr.theogiraudet.tp.spring.dto.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
//TODO remove
@CrossOrigin
public class ModelResource {

    @Autowired
    private ModelDao dao;

    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Model>> getModels() {
        return ResponseEntity.ok().body(dao.getModels());
    }

    @GetMapping(value = "/model/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getModel(@PathVariable("name") String name) {
        return dao.getModel(name).map(ResponseEntity.ok()::body).orElse(ResponseEntity.notFound().build());
    }

}
