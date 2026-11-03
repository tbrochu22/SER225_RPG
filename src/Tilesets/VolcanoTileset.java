package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

// A lava / rock / volcano tileset defined in the VolcanoTileset.png file
// VolcanoTileset.png is a 6x4 sheet of 16x16 tiles with a 1px gap between them, laid out exactly like DesertTileset.png
// Every tile number means the same kind of thing as in DesertTileset (0 ground, 1 path, 2 boulder, 3 cliff wall, 14-18 the big
// landmark), so a desert map file can be switched over to this tileset and still make sense.
// Unlike the desert pyramid, the volcano can NOT be entered: every volcano tile is solid.
public class VolcanoTileset extends Tileset {

    public VolcanoTileset() {
        super(ImageLoader.load("VolcanoTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // index 0 -- grey volcanic ash (base terrain, passable)
        Frame ashFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();
        mapTiles.add(new MapTileBuilder(ashFrame));

        // index 1 -- scorched path (passable)
        mapTiles.add(plainTile(0, 1, TileType.PASSABLE));

        // index 2 -- obsidian boulder (not passable)
        mapTiles.add(plainTile(0, 2, TileType.NOT_PASSABLE));

        // index 3 -- basalt cliff wall with glowing seams, used to border the map (not passable)
        mapTiles.add(plainTile(1, 0, TileType.NOT_PASSABLE));

        // index 4 -- ash with glowing embers scattered on top (passable)
        mapTiles.add(overlayTile(ashFrame, 1, 1, TileType.PASSABLE));

        // index 5 -- obsidian crystal spire (not passable)
        mapTiles.add(overlayTile(ashFrame, 1, 2, TileType.NOT_PASSABLE));

        // index 6 -- cooled, ropy lava rock (passable)
        mapTiles.add(plainTile(2, 0, TileType.PASSABLE));

        // index 7 -- ground split by glowing magma cracks (passable)
        mapTiles.add(plainTile(2, 1, TileType.PASSABLE));

        // index 8 -- charred skull and bone lying in the ash (passable, decoration)
        mapTiles.add(overlayTile(ashFrame, 2, 2, TileType.PASSABLE));

        // ---------------- more volcanic scenery ----------------

        // index 9 -- smoldering ember pile (not passable)
        mapTiles.add(overlayTile(ashFrame, 0, 3, TileType.NOT_PASSABLE));

        // index 10 -- charred dead tree (not passable)
        mapTiles.add(overlayTile(ashFrame, 0, 4, TileType.NOT_PASSABLE));

        // index 11 -- fire geyser / steam vent (not passable)
        mapTiles.add(overlayTile(ashFrame, 0, 5, TileType.NOT_PASSABLE));

        // index 12 -- lava pool (not passable)
        mapTiles.add(plainTile(1, 3, TileType.NOT_PASSABLE));

        // index 13 -- broken basalt column (not passable)
        mapTiles.add(overlayTile(ashFrame, 1, 4, TileType.NOT_PASSABLE));

        // ---------------- volcano (outside) ----------------
        // build it in rows that get 2 tiles wider each step down, same as the desert pyramid:
        //        14
        //     15 16 17
        //  15 16 16 16 17
        //  15 16 18 16 17   <- 18 = lava spilling out of the base, put it in the bottom row

        // index 14 -- volcano peak with the glowing crater (not passable)
        mapTiles.add(plainTile(1, 5, TileType.NOT_PASSABLE));

        // index 15 -- volcano left slope (not passable)
        mapTiles.add(plainTile(2, 3, TileType.NOT_PASSABLE));

        // index 16 -- volcano rock with lava streaks (not passable)
        mapTiles.add(plainTile(2, 4, TileType.NOT_PASSABLE));

        // index 17 -- volcano right slope (not passable)
        mapTiles.add(plainTile(2, 5, TileType.NOT_PASSABLE));

        // index 18 -- volcano base with lava spilling out of a crack (not passable -- the volcano can't be entered)
        mapTiles.add(plainTile(3, 0, TileType.NOT_PASSABLE));

        // ---------------- rock / cave scenery (walls and floors for ruins, caves or mazes) ----------------

        // index 19 -- dark basalt flagstones (passable)
        Frame flagstoneFrame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();
        mapTiles.add(new MapTileBuilder(flagstoneFrame));

        // index 20 -- dark rock wall (not passable)
        mapTiles.add(plainTile(3, 2, TileType.NOT_PASSABLE));

        // index 21 -- rock wall with glowing magma veins (not passable)
        mapTiles.add(plainTile(3, 3, TileType.NOT_PASSABLE));

        // index 22 -- rock wall with a lavafall pouring down it (not passable)
        mapTiles.add(plainTile(3, 4, TileType.NOT_PASSABLE));

        // index 23 -- glowing fire crystals on the flagstones (not passable)
        mapTiles.add(overlayTile(flagstoneFrame, 3, 5, TileType.NOT_PASSABLE));

        return mapTiles;
    }

    // a tile that is just one image from the sheet
    private MapTileBuilder plainTile(int row, int column, TileType tileType) {
        Frame frame = new FrameBuilder(getSubImage(row, column))
                .withScale(tileScale)
                .build();
        return new MapTileBuilder(frame).withTileType(tileType);
    }

    // a tile drawn on top of a base tile (the overlay image uses magenta for its see-through parts)
    private MapTileBuilder overlayTile(Frame baseFrame, int row, int column, TileType tileType) {
        Frame overlayFrame = new FrameBuilder(getSubImage(row, column))
                .withScale(tileScale)
                .build();
        return new MapTileBuilder(baseFrame)
                .withTopLayer(overlayFrame)
                .withTileType(tileType);
    }
}