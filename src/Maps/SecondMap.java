package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Boss2;
import NPCs.Nomad;
import Scripts.SecondMap.Boss2Script;
import Scripts.SecondMap.NomadScript;
import Tilesets.DesertTileset;

import java.util.ArrayList;

// The map the player is transported to after beating Boss1 and walking off the end of the rocky path.
// The path runs from the start (top) down the left side of the pyramid (middle right), branches right to its doorway, then heads to Boss2 (bottom left).
// Walking up into the pyramid doorway (tile 31, 17) goes inside (PyramidMap). The boulders in the bottom wall below Boss2 (tiles 4-5 of row 29) open once Boss2 is beaten.
// Walking back up through the opening in the top wall (tiles 19-20 of row 0) returns to the rocky path.
// Desert area. Placeholder layout for now -- add NPCs, flowers, and triggers here the same way RockyPathMap does.
public class SecondMap extends Map {

    public SecondMap() {
        super("second_map.txt", new DesertTileset());
        this.playerStartPosition = getMapTile(19, 1).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // nomad greets the player right where they arrive, just right of the path
        Nomad nomad = new Nomad(1, getMapTile(21, 2).getLocation());
        nomad.setInteractScript(new NomadScript());
        npcs.add(nomad);

        // second boss waits in the bottom-left corner at the end of the path (after the pyramid)
        Boss2 boss2 = new Boss2(2, getMapTile(2, 26).getLocation());
        boss2.setInteractScript(new Boss2Script());
        npcs.add(boss2);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
    }
}