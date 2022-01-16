package fr.theogiraudet.dispenser_api.domain;

import lombok.Data;

import java.util.List;

/**
 * POJO for official Minecraft Manifest JSON
 */
public @Data class Manifest {

    /**
     * the {@link Latest} released Minecraft versions
     */
    private Latest latest;

    /**
     * The list of all version of Minecraft
     */
    private List<VersionInformation> versions;

    /**
     * A tuple representing the latest Minecraft release and snapshot
     */
    @Data
    static class Latest {
        /**
         * The latest Minecraft release
         */
        private String release;

        /**
         * The latest Minecraft snapshot
         */
        private String snapshot;
    }

}
