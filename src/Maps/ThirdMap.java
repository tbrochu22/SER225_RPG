package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.VolcanoTileset;

import java.util.ArrayList;

// The volcano area, reached through the exit that opens in the desert (SecondMap) after Boss2 is beaten.
// Same size as the desert (40x30). The volcano sits on the left side, a lava river runs from its base to the bottom-right corner,
// and the path winds from the opening in the top-right wall (tiles 35-36 of row 0) down to the bottom-left corner.
// Where the path crosses the lava river it's a cooled crust of path tiles, so the player can walk over it.
// Walking back up through the top-right opening returns to the desert.
public class ThirdMap extends Map {

    public ThirdMap() {
        super("third_map.txt", new VolcanoTileset());
        this.playerStartPosition = getMapTile(35, 1).getLocation();
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