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
// Walking back up through the opening in the top wall (tiles 9-10 of row 0) returns to the rocky path.
// Desert area. Placeholder layout for now -- add NPCs, flowers, and triggers here the same way RockyPathMap does.
public class SecondMap extends Map {

    public SecondMap() {
        super("second_map.txt", new DesertTileset());
        this.playerStartPosition = getMapTile(9, 1).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // nomad greets the player right where they arrive, just right of the path
        Nomad nomad = new Nomad(1, getMapTile(11, 2).getLocation());
        nomad.setInteractScript(new NomadScript());
        npcs.add(nomad);

        // second boss waits beside the end of the path, facing it
        Boss2 boss2 = new Boss2(2, getMapTile(11, 12).getLocation());
        boss2.setExistenceFlag("hasBeatenBoss2");
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