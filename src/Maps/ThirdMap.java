package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.VolcanoTileset;
import Utils.Point;

import java.util.ArrayList;
import NPCs.RoamingEnemy;
import Screens.BossScreen;
import NPCs.Critter;
import NPCs.Villager;
import Scripts.SimpleTextScript;
import Utils.Direction;
import NPCs.Imp;
import Scripts.ThirdMap.ImpScript;

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
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
    }
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        npcs.add(new RoamingEnemy(20, getMapTile(28, 7).getLocation(), "EmberHawk.png", "Ember Hawk"));     // first bend below the entrance
        npcs.add(new RoamingEnemy(21, getMapTile(23, 10).getLocation(), "MagmaGolem.png", "Magma Golem"));  // open ash right of the path, level with the volcano
        npcs.add(new RoamingEnemy(22, getMapTile(17, 17).getLocation(), "MagmaGolem.png", "Magma Golem"));  // just before the lava crossing
        npcs.add(new RoamingEnemy(23, getMapTile(7, 24).getLocation(), "EmberHawk.png", "Ember Hawk"));     // bottom-left end of the path

        Villager hermit = new Villager(12, getMapTile(7, 19).getLocation(), "Hermit.png", Direction.RIGHT);
        hermit.setInteractScript(new SimpleTextScript(new String[] {
                "Few travelers make it this far...",
                "I came out here for some peace and quiet.\nThen the volcano started rumbling. Ha!"
        }));
        npcs.add(hermit);

        Critter salamander = new Critter(13, getMapTile(12, 27).getLocation(), "FireSalamander.png", 240);
        salamander.setInteractScript(new SimpleTextScript("*The salamander's tail crackles with flame.*"));
        npcs.add(salamander);

        Imp imp = new Imp(10, getMapTile(37, 2).getLocation());
        imp.setInteractScript(new ImpScript());
        npcs.add(imp);
        return npcs;
}
}