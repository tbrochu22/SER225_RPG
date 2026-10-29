package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.DesertTileset;

import java.util.ArrayList;

// The inside of the pyramid in the desert (SecondMap).
// The player enters by walking up into the pyramid's doorway, and leaves through the doorway in the bottom wall (tile 9 of row 13).
// Uses the inside tiles of DesertTileset: floor (19), walls (20), hieroglyph walls (21), torch walls (22), urns (23), doorway (18).
public class PyramidMap extends Map {

    public PyramidMap() {
        super("pyramid_map.txt", new DesertTileset());
        this.playerStartPosition = getMapTile(9, 11).getLocation();
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