package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.VolcanoTileset;

import java.util.ArrayList;

// The inside of the cave in the volcano (ThirdMap).
// The player enters by walking up into the cave's doorway, and leaves through the doorway in the bottom wall
public class CaveMap extends Map {

    public CaveMap() {
        super("cave_map.txt", new VolcanoTileset());
        // Don't know the exact coordinates of the cave entrance yet, so just set the player start position to (0, 0) for now
        this.playerStartPosition = getMapTile(0, 0).getLocation();
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