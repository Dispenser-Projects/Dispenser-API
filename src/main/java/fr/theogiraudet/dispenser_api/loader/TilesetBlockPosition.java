package fr.theogiraudet.dispenser_api.loader;

import java.util.HashMap;


public class TilesetBlockPosition {

    private static HashMap<String,HashMap<String, int[]>> recordPosition = new HashMap<>();

    public static void addVersion(String version) {
        recordPosition.put(version, new HashMap<>());
    }

    public static void addPosition(String version, String blockName, int[] position) {
        recordPosition.get(version).put(blockName, position);
    }
    
    public static int[] getPosition(String version, String blockName) {
        return recordPosition.get(version).get(blockName);
    }
}