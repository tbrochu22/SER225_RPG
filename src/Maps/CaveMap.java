package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Boss3;
import Scripts.ThirdMap.Boss3Script;
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
        ArrayList<NPC> npcs = new ArrayList<>();
        Boss3 boss3 = new Boss3(3, getMapTile(9, 6).getLocation());
        boss3.setExistenceFlag("hasBeatenBoss3");
        boss3.setInteractScript(new Boss3Script());
        npcs.add(boss3);
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