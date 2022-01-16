package fr.theogiraudet.dispenser_api.loader;

import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import fr.theogiraudet.dispenser_api.domain.VersionInformation;

/**
 * An interface to define the loader of Minecraft assets
 */
public interface AssetLoader {

    /**
     * Load specified assets from specified version
     * @param infos the {@link VersionInformation} from which to extract the assets
     * @param assets the assets to extract
     */
    void loadVersion(VersionInformation infos, MinecraftAsset... assets);

}
