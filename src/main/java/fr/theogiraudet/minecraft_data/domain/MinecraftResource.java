package fr.theogiraudet.minecraft_data.domain;

/**
 * The interface representing a Minecraft Resource Type (i.e. an asset type or a data type)
 */
public interface MinecraftResource {

    /**
     * @return the id of the asset type
     */
    String getId();

    /**
     * @param version the version to use to build the URI
     * @return the URI to the function to get all assets for this type and for the specified version
     */
    String getAllUri(String version);


}
