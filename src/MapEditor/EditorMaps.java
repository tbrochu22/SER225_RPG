package MapEditor;

import Level.Map;
import Maps.RockyPathMap;
import Maps.TestMap;
import Maps.TitleScreenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("RockyPathMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "RockyPathMap":
                return new RockyPathMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}