package Maps;

import Level.EnhancedMapTile;
import EnhancedMapTiles.Gemstone;
import EnhancedMapTiles.PushableRock;
import Level.Map;
import Level.MapTile;
import Level.NPC;
import Level.Player;
import Level.Trigger;
import Scripts.Pyramid.GemScript;
import Tilesets.DesertTileset;

import java.util.ArrayList;

// The inside of the pyramid in the desert (SecondMap).
// The player enters by walking up into the pyramid's doorway, and leaves through the doorway in the bottom wall (tile 9 of row 13).
// Uses the inside tiles of DesertTileset: floor (19), walls (20), hieroglyph walls (21), torch walls (22), urns (23), doorway (18).
public class PyramidMap extends Map {
    private static final int[][] ROCK_START_TILES = {
            {5, 10},
            {13, 10},
            {5, 6},
            {13, 6}
    };

    public PyramidMap() {
        super("pyramid_map.txt", new DesertTileset());
        this.playerStartPosition = getMapTile(9, 11).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        for (int[] startTile : ROCK_START_TILES) {
            enhancedMapTiles.add(new PushableRock(getMapTile(startTile[0], startTile[1]).getLocation()));
        }
        return enhancedMapTiles;
    }

    Gemstone gemstone = new Gemstone(getMapTile(9, 6).getLocation());

    @Override
    public void update(Player player) {
        super.update(player);

        if (!hasGemstone() && areAllRocksOnTileIndex(1)) {
            addEnhancedMapTile(gemstone);
            gemstone.setInteractScript(new GemScript());
            gemstone.setExistenceFlag("hasGem");
            getTextbox().addText("A gemstone has appeared in the center of the pyramid!");
            getTextbox().setIsActive(true);
            player.lock();
        }
    }

    private boolean areAllRocksOnTileIndex(int tileIndex) {
        int rockCount = 0;
        for (EnhancedMapTile tile : enhancedMapTiles) {
            if (tile instanceof PushableRock) {
                rockCount++;
                if (!((PushableRock) tile).isOnTileIndex(tileIndex)) {
                    return false;
                }
            }
        }

        return rockCount == ROCK_START_TILES.length;
    }

    public void resetRocksIfPuzzleIncomplete() {
        int rockIndex = 0;
        for (EnhancedMapTile tile : enhancedMapTiles) {
            if (tile instanceof PushableRock && rockIndex < ROCK_START_TILES.length) {
                int[] startTile = ROCK_START_TILES[rockIndex++];
                PushableRock rock = (PushableRock) tile;
                if (!rock.isOnTileIndex(1)) {
                    MapTile startingMapTile = getMapTile(startTile[0], startTile[1]);
                    rock.setLocation(startingMapTile.getX(), startingMapTile.getY());
                }
            }
        }
    }

    private boolean hasGemstone() {
        for (EnhancedMapTile tile : enhancedMapTiles) {
            if (tile instanceof Gemstone) {
                return true;
            }
        }
        return false;
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