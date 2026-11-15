package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.VolcanoTileset;

import java.util.ArrayList;

// The inside of the cave in the volcano (ThirdMap).
// The player enters by walking up into the cave gate (once it has opened), and leaves through the opening in the bottom wall (tiles 9-10 of row 15)
public class CaveMap extends Map {

    public CaveMap() {
        super("cave_map.txt", new VolcanoTileset());
        this.playerStartPosition = getMapTile(9, 13).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
    }
}