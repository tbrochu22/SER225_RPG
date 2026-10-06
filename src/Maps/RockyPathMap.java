package Maps;

import EnhancedMapTiles.Rflower;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Archer;
import NPCs.Boss1;
import NPCs.Mage;
import Scripts.TestMap.ArcherScript;
import Scripts.TestMap.MageScript;
import Scripts.TestMap.Boss1Script;
import Tilesets.RockyTileset;

import java.util.ArrayList;

// A rocky outdoor map with a winding dirt path cutting through it, bordered by cliff walls.
// Mage greets the player at the start of the path, Archer says hi partway down, and Boss1 waits near the end.
// Small grass patches with flowers are scattered around the rocky ground for extra variety.
public class RockyPathMap extends Map {

    public RockyPathMap() {
        super("rocky_path_map.txt", new RockyTileset());
        this.playerStartPosition = getMapTile(16, 1).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        // flowers planted in a few of the grass patches scattered around the map
        int[][] flowerSpots = {
                {17, 34}, {20, 15}, {17, 14}, {22, 27}, {23, 3}, {18, 7}, {21, 4}
        };
        for (int[] spot : flowerSpots) {
            Rflower flower = new Rflower(getMapTile(spot[0], spot[1]).getLocation());
            enhancedMapTiles.add(flower);
        }

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // mage greets the player right at the start of the path
        Mage mage = new Mage(1, getMapTile(19, 2).getLocation());
        mage.setInteractScript(new MageScript());
        npcs.add(mage);

        // archer says hi partway down the path
        Archer archer = new Archer(2, getMapTile(11, 20).getLocation());
        archer.setInteractScript(new ArcherScript());
        npcs.add(archer);

        // boss waits near the end of the path
        Boss1 boss1 = new Boss1(3, getMapTile(8, 37).getLocation());
        boss1.setInteractScript(new Boss1Script());
        npcs.add(boss1);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
        // no interactable tiles yet
    }
}