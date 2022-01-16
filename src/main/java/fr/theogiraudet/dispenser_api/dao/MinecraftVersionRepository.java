package fr.theogiraudet.dispenser_api.dao;

import fr.theogiraudet.dispenser_api.domain.VersionInformation;
import fr.theogiraudet.dispenser_api.domain.VersionType;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import java.util.Optional;

/**
 * Interface to manage VersionInformation from database
 */
@NoRepositoryBean
public interface MinecraftVersionRepository extends PagingAndSortingRepository<VersionInformation, String>, QueryByExampleExecutor<VersionInformation> {

    /**
     * Insert the specified {@link VersionInformation} of not exist in the database, overwrite the existing otherwise
     * @param version the {@link VersionInformation} to upsert
     */
    void upsert(VersionInformation version);

    /**
     * @param version the version to get from the database
     * @return the corresponding {@link VersionInformation} instance if exists, {@link Optional#empty()} otherwise
     */
    Optional<VersionInformation> getVersion(String version);

    /**
     * @param versionType the type of the latest version wanted
     * @return the latest version with the specified type
     */
    VersionInformation getLatestVersion(VersionType versionType);

}
