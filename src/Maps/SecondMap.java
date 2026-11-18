package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Boss2;
import NPCs.Nomad;
import Scripts.SecondMap.Boss2Script;
import Scripts.SecondMap.NomadScript;
import Scripts.SecondMap.FishermanScript;
import Tilesets.DesertTileset;
import java.util.ArrayList;
import NPCs.RoamingEnemy;
import EnhancedMapTiles.DesertHouse1;
import EnhancedMapTiles.DesertHouse2;
import EnhancedMapTiles.DesertHouse3;
import NPCs.Critter;
import NPCs.Villager;
import Scripts.SimpleTextScript;
import Utils.Direction;

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
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        enhancedMapTiles.add(new DesertHouse1(getMapTile(12, 2).getLocation()));
        enhancedMapTiles.add(new DesertHouse2(getMapTile(14, 9).getLocation()));
        enhancedMapTiles.add(new DesertHouse3(getMapTile(34, 22).getLocation()));
        return enhancedMapTiles;
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
        boss2.setExistenceFlag("hasBeatenBoss2");
        boss2.setInteractScript(new Boss2Script());
        npcs.add(boss2);
        Villager fisher = new Villager(10, getMapTile(9, 6).getLocation(), "OasisFisher.png", Direction.LEFT);
        fisher.setInteractScript(new FishermanScript());
        npcs.add(fisher);
        Villager merchant = new Villager(11, getMapTile(34, 18).getLocation(), "Merchant.png", Direction.LEFT);
        merchant.setInteractScript(new SimpleTextScript(new String[] {
                "Welcome, welcome! Finest wares this side\nof the dunes!",
                "...Well, I WOULD sell you something, but that\nred brute down south scared off my camel."
        }));
        npcs.add(merchant);
        Villager archaeologist = new Villager(12, getMapTile(27, 13).getLocation(), "Archaeologist.png", Direction.LEFT);
        archaeologist.setInteractScript(new SimpleTextScript(new String[] {
                "Fascinating... these columns are thousands\nof years old!",
                "I'd go in the pyramid myself, but I'm more of a\n'look from a safe distance' scholar."
        }));
        npcs.add(archaeologist);
        Critter lizard = new Critter(13, getMapTile(18, 23).getLocation(), "SandLizard.png", 200);
        lizard.setInteractScript(new SimpleTextScript("*The lizard flicks its tongue at you.*"));
        npcs.add(lizard);
        npcs.add(new RoamingEnemy(20, getMapTile(18, 12).getLocation(), "SandScorpion.png", "Sand Scorpion"));   // left of the path, below the arrival
        npcs.add(new RoamingEnemy(21, getMapTile(33, 7).getLocation(), "DesertCobra.png", "Desert Cobra"));     // dunes in the top right
        npcs.add(new RoamingEnemy(22, getMapTile(9, 19).getLocation(), "DesertCobra.png", "Desert Cobra"));     // on the path heading to Boss2
        npcs.add(new RoamingEnemy(23, getMapTile(34, 25).getLocation(), "SandScorpion.png", "Sand Scorpion"));  // bottom right, near the bones
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