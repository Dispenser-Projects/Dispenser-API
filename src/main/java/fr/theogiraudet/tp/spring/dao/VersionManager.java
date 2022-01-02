package fr.theogiraudet.tp.spring.dao;

import fr.theogiraudet.tp.spring.dao.from_disk.VersionLoader;
import fr.theogiraudet.tp.spring.domain.VersionInformation;
import fr.theogiraudet.tp.spring.utils.Loading;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VersionManager {

    private Map<String, Version> loadedVersion = new ConcurrentHashMap<>();
    private Map<String, VersionInformation> existingVersions = new ConcurrentHashMap<>();
    private final VersionLoader versionLoader;

    public VersionManager(VersionLoader versionLoader) {
        this.versionLoader = versionLoader;
    }

    public Loading<Version> getVersion(String version) {
        return Loading.loadNullable(loadedVersion.get(version))
                .loadIfAbsent(existingVersions.containsKey(version))
                .ifLoading(() -> loadVersion(version));
    }

    public void loadVersion(String id) {
        final Version version = versionLoader.loadVersion(id);
        loadedVersion.put(version.getId(), version);
    }

    public void update() {
        versionLoader.update(getLoadedVersion(), getExistingVersions(), this);
    }

    private Map<String, Version> getLoadedVersion() {
        return Collections.unmodifiableMap(loadedVersion);
    }

    private Map<String, VersionInformation> getExistingVersions() {
        return Collections.unmodifiableMap(existingVersions);
    }

    void setLoadedVersion(Map<String, Version> loadedVersion) {
        this.loadedVersion = loadedVersion;
    }

    void setExistingVersions(Map<String, VersionInformation> existingVersions) {
        this.existingVersions = existingVersions;
    }
}
