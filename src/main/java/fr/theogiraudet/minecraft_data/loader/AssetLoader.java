package fr.theogiraudet.minecraft_data.loader;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;

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
