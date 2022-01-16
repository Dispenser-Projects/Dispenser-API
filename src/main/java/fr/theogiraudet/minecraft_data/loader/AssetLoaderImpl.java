package fr.theogiraudet.minecraft_data.loader;

import fr.theogiraudet.minecraft_data.dao.MinecraftResourceRepository;
import fr.theogiraudet.minecraft_data.dao.MinecraftVersionRepository;
import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An implementation of {@link AssetLoader} to load hashed assets in a database
 */
@Component
public class AssetLoaderImpl implements AssetLoader {

    /** The path where download jar version and temporary extract data */
    @Value("${minecraft-data.tmp-folder}")
    private String tmpPath;

    /** The path where save extracted data */
    @Value("${minecraft-data.data-folder}")
    private String dataPath;

    /** The repository to used to get and save new resources */
    private final MinecraftResourceRepository repository;

    /** The repository to used to register an asset type to a version */
    private final MinecraftVersionRepository versionRepository;

    private final Logger logger = LoggerFactory.getLogger(AssetLoaderImpl.class);

    /**
     * Create a new AssetLoaderImpl
     * @param repository the repository to used to get and save new resources
     * @param versionRepository the repository to used to register an asset type to a version
     */
    @Autowired
    public AssetLoaderImpl(MinecraftResourceRepository repository, MinecraftVersionRepository versionRepository) {
        this.repository = repository;
        this.versionRepository = versionRepository;
    }

    /**
     * Load specified assets from specified version
     * @param infos the {@link VersionInformation} from which to extract the assets
     * @param assets the assets to extract
     */
    @Override
    public void loadVersion(VersionInformation infos, MinecraftAsset... assets) {
        final var startingTime = System.currentTimeMillis();
        if (logger.isDebugEnabled())
            logger.debug("Start loading {} for {}...", infos.getId(), Arrays.stream(assets).map(MinecraftAsset::getId).collect(Collectors.joining(", ")));
        try {
            // Cleanup temporary folder, download the jar corresponding to the specified version
            // and extract/add to the database all specified assets in the data folder
            final var path = Path.of(tmpPath);
            FileUtils.deleteDirectory(new File(path.toString()));
            Files.createDirectories(path);
            final File archive = download(infos, path);
            extractAll(archive, assets);
            addAllToDb(infos, assets);
            FileUtils.deleteDirectory(new File(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("{} loaded in {} ms", infos.getId(), System.currentTimeMillis() - startingTime);
    }

    /**
     * Add to the database all extracted assets and move them to the data folder
     * @param version the version from which add the data to database
     * @param assetTypes the data type to add
     * @throws IOException if the version folder doesn't exist on temporary folder
     */
    private void addAllToDb(VersionInformation version, MinecraftAsset... assetTypes) throws IOException {
        logger.debug("Loading data in database");
        for (var asset : assetTypes) {
            final var oldPath = tmpPath + File.separatorChar + asset.getId();
            final var newPath = dataPath + File.separatorChar + asset.getId();
            Files.createDirectories(Path.of(newPath));
            final var files = new File(oldPath).listFiles();
            if(files == null)
                throw new IOException("'" + oldPath + "' doesn't exist.");
            Arrays.stream(files).forEach(f -> addToDb(f, version.getId(), asset, newPath));
            version.addLoadedAsset(asset);
        }
        versionRepository.upsert(version);
    }

    /**
     * Add a single asset to the database. If the asset is already presents in the database, add only version and file name
     * to the existing HashedAsset entry, else, create a new entry in the database
     * @param file the file to hash, add to the database and to move in the data folder
     * @param version the version from which add the file to database
     * @param assetType the asset type of the data to add
     * @param newPath the path whee move the file
     */
    private void addToDb(File file, String version, MinecraftAsset assetType, String newPath) {
        final String[] nameSplit = file.getName().split("\\.", 2);
        try {
            final String hash;
            try (var stream = new FileInputStream(file)) {
                hash = DigestUtils.md5Hex(stream);
            }
            if (repository.hashExists(assetType, hash))
                repository.addToExistingHash(assetType, hash, version, nameSplit[0]);
            else {
                repository.addNewAssetHash(assetType, version, nameSplit[0], hash);
                Files.move(file.toPath(), Path.of(newPath + File.separatorChar + hash + "." + nameSplit[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Download the specified version
     * @param informations the {@link VersionInformation} from which to get download url
     * @param path the path where download the jar
     * @return the downloaded jar
     * @throws IOException if the url is invalid or if the destination path doesn't exist
     */
    private File download(VersionInformation informations, Path path) throws IOException {
        logger.debug("Downloading {}.jar from {}", informations.getId(), informations.getUrl());
        URL website = new URL(informations.getUrl());
        final var filePath = path.toString() + File.separatorChar + informations.getId() + ".jar";
        final var file = new File(filePath);
        FileUtils.copyURLToFile(website, file);
        return file;
    }

    /**
     * Extract all assets from the jar file.
     * If the asset isn't found on the jar, ignore it.
     * @param file the jar file
     * @param assets all assets to extract from the file
     * @throws IOException if the file doesn't exist
     */
    private void extractAll(File file, MinecraftAsset... assets) throws IOException {
        try (final ZipFile archive = new ZipFile(file)) {
            for (var asset : assets) {
                if (logger.isDebugEnabled())
                    logger.debug("Extracting {} from {}", asset.getPathInJar() + "/*." + asset.getExtension(), file.getName());

                final var destination = Path.of(tmpPath + File.separatorChar + asset.getId());
                Files.createDirectories(destination);
                final var files = archive
                        .getFileHeaders()
                        .parallelStream()
                        .filter(a -> a.getFileName().startsWith(asset.getPathInJar()))
                        .filter(a -> a.getFileName().endsWith('.' + asset.getExtension()))
                        .toList();

                for (var fileHeader : files) {
                    final var fileName = fileHeader.getFileName().split("/");
                    archive.extractFile(fileHeader, destination.toString(), fileName[fileName.length - 1]);
                }
            }
        }
    }
}
