package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.RockyTileset;

import java.util.ArrayList;

// The map the player is transported to after beating Boss1 and walking off the end of the rocky path.
// Placeholder layout for now -- add NPCs, flowers, and triggers here the same way RockyPathMap does.
public class SecondMap extends Map {

    public SecondMap() {
        super("second_map.txt", new RockyTileset());
        this.playerStartPosition = getMapTile(9, 1).getLocation();
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