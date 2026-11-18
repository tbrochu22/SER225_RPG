package MapEditor;

import Level.Map;
import Maps.PyramidMap;
import Maps.RockyPathMap;
import Maps.SecondMap;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.ThirdMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("RockyPathMap");
            add("SecondMap");
            add("ThirdMap");
            add("PyramidMap");
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
            case "SecondMap":
                return new SecondMap();
            case "ThirdMap":
                return new ThirdMap();
            case "PyramidMap":
                return new PyramidMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}