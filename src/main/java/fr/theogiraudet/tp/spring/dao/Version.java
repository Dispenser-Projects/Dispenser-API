package fr.theogiraudet.tp.spring.dao;

import fr.theogiraudet.tp.spring.domain.VersionInformation;

import java.util.Optional;

public interface Version {

    Optional<String> getTexture(String textureName);
    Optional<String> getModel(String modelName);
    VersionInformation getInformations();
    String getId();

}
