package Maps;

import EnhancedMapTiles.Cookie;
import EnhancedMapTiles.Deodorant;
import EnhancedMapTiles.Flower;
import EnhancedMapTiles.Gift;
import EnhancedMapTiles.GlassEye;
import EnhancedMapTiles.Rflower;
import EnhancedMapTiles.ToyTrain;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Archer;
import NPCs.Boss1;
import NPCs.Mage;
import Scripts.TestMap.ArcherScript;
import Scripts.TestMap.Boss1Script;
import Scripts.TestMap.CookieScript;
import Scripts.TestMap.DeodorantScript;
import Scripts.TestMap.GiftScript;
import Scripts.TestMap.GlassEyeScript;
import Scripts.TestMap.MageScript;
import Scripts.TestMap.ToyTrainScript;
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
        int[][] flowerSpotsR = {
                // scattered along the path (none sit on the path itself)
                {17, 34}, {20, 15}, {22, 27}, {23, 3}, {21, 4},
                // flower field in the top-left grass
                {1, 1}, {5, 1}, {2, 2}, {8, 2}, {3, 3}, {2, 4}, {12, 4}, {3, 5}, {6, 6}
        };
        for (int[] spot : flowerSpotsR) {
            Rflower flower = new Rflower(getMapTile(spot[0], spot[1]).getLocation());
            enhancedMapTiles.add(flower);
        }

        int[][] flowerSpotsY = {
                {30, 2}, {30, 4},
                // flower field in the top-left grass
                {3, 1}, {11, 1}, {6, 2}, {12, 2}, {7, 3}, {8, 4}, {1, 5}, {7, 5}, {10, 6}
        };
        for (int[] spot : flowerSpotsY) {
            Flower flower = new Flower(getMapTile(spot[0], spot[1]).getLocation());
            enhancedMapTiles.add(flower);
        }

        GlassEye glassEye = new GlassEye(getMapTile(13, 13).getLocation());
        glassEye.setInteractScript(new GlassEyeScript());
        enhancedMapTiles.add(glassEye);

        Cookie cookie = new Cookie(getMapTile(20, 21).getLocation());
        cookie.setInteractScript(new CookieScript());
        enhancedMapTiles.add(cookie);

        Deodorant deodorant = new Deodorant(getMapTile(29, 29).getLocation());
        deodorant.setInteractScript(new DeodorantScript());
        enhancedMapTiles.add(deodorant);

        ToyTrain toyTrain = new ToyTrain(getMapTile(3, 30).getLocation());
        toyTrain.setInteractScript(new ToyTrainScript());
        enhancedMapTiles.add(toyTrain);

        Gift gift = new Gift(getMapTile(7, 35).getLocation());
        gift.setInteractScript(new GiftScript());
        gift.setExistenceFlag("hasGift");
        enhancedMapTiles.add(gift);


        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // mage greets the player right at the start of the path
        Mage mage = new Mage(1, getMapTile(19, 2).getLocation());
        mage.setInteractScript(new MageScript());
        npcs.add(mage);

        // archer says hi partway down the path, standing just right of it
        Archer archer = new Archer(2, getMapTile(25, 20).getLocation());
        archer.setInteractScript(new ArcherScript());
        npcs.add(archer);

        // boss waits near the end of the path
        Boss1 boss1 = new Boss1(3, getMapTile(25, 37).getLocation());
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