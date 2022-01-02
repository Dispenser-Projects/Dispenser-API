package fr.theogiraudet.tp.spring.dao.from_disk;

import fr.theogiraudet.tp.spring.dao.Version;
import fr.theogiraudet.tp.spring.dao.VersionManager;
import fr.theogiraudet.tp.spring.domain.VersionInformation;

import java.util.Map;

public interface VersionLoader {

    Version loadVersion(String id);
    void update(Map<String, Version> loadedVersion, Map<String, VersionInformation> existingVersions, VersionManager manager);

}
