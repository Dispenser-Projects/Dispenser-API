package fr.theogiraudet.minecraft_data.dao.mongo;

import fr.theogiraudet.minecraft_data.dao.MinecraftVersionRepository;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import fr.theogiraudet.minecraft_data.domain.VersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Implementation of MinecraftVersionRepository for MongoDB
 */
@Component
public class MinecraftVersionRepositoryMongo implements MinecraftVersionRepository {

    /**
     * Just a proxy of MinecraftVersionRepoMongo to abstract the database implementation
     */
    private final MinecraftVersionRepoMongo repository;

    /**
     * Create a {@link MinecraftVersionRepositoryMongo}
     * @param repository the repository to use for this instance
     */
    @Autowired
    public MinecraftVersionRepositoryMongo(MinecraftVersionRepoMongo repository) {
        this.repository = repository;
    }

    /**
     * @param version the version to get from the database
     * @return the corresponding {@link VersionInformation} instance if exists, {@link Optional#empty()} otherwise
     */
    @Override
    public Optional<VersionInformation> getVersion(String version) {
        return repository.findById(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VersionInformation getLatestVersion(VersionType versionType) {
        final var example = new VersionInformation();
        example.setVersionType(versionType);
        return repository.findAll(Example.of(example), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "releaseTime")))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Insert the specified {@link VersionInformation} of not exist in the database, overwrite the existing otherwise
     * @param version the {@link VersionInformation} to upsert
     */
    @Override
    public void upsert(VersionInformation version) {
        repository.save(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<VersionInformation> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<VersionInformation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> S save(S s) {
        return repository.save(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> Iterable<S> saveAll(Iterable<S> iterable) {
        return repository.saveAll(iterable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<VersionInformation> findById(String s) {
        return repository.findById(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(String s) {
        return repository.existsById(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<VersionInformation> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<VersionInformation> findAllById(Iterable<String> iterable) {
        return repository.findAllById(iterable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(String s) {
        repository.deleteById(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(VersionInformation information) {
        repository.delete(information);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllById(Iterable<? extends String> iterable) {
        repository.deleteAllById(iterable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Iterable<? extends VersionInformation> iterable) {
        repository.deleteAll(iterable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> Optional<S> findOne(Example<S> example) {
        return repository.findOne(example);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> Iterable<S> findAll(Example<S> example) {
        return repository.findAll(example);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> Iterable<S> findAll(Example<S> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> Page<S> findAll(Example<S> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> long count(Example<S> example) {
        return repository.count(example);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends VersionInformation> boolean exists(Example<S> example) {
        return repository.exists(example);
    }

    /**
     * @param type the version type to use for the example
     * @return an {@link Example} with the specified type
     */
    private Example<VersionInformation> getExample(VersionType type) {
        if (type == null)
            return null;
        final var version = new VersionInformation();
        version.setVersionType(type);
        return Example.of(version);
    }
}
