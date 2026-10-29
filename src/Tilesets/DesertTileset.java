package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

// A desert tileset (sand, sandstone, cactus, oasis, pyramid outside + inside) defined in the DesertTileset.png file
// DesertTileset.png is a 6x4 sheet of 16x16 tiles with a 1px gap between them
// Tile indexes line up with RockyTileset where they can (0 ground, 1 path, 2 rock, 3 cliff wall, 4 pebbles)
// so a map can be switched between the two tilesets without breaking
public class DesertTileset extends Tileset {

    public DesertTileset() {
        super(ImageLoader.load("DesertTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // index 0 -- sand (base terrain, passable)
        Frame sandFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder sandTile = new MapTileBuilder(sandFrame);

        mapTiles.add(sandTile);

        // index 1 -- packed dirt path (passable)
        Frame pathFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder pathTile = new MapTileBuilder(pathFrame);

        mapTiles.add(pathTile);

        // index 2 -- sandstone rock (not passable)
        Frame rockFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder rockTile = new MapTileBuilder(rockFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(rockTile);

        // index 3 -- layered sandstone cliff wall, used to border the map (not passable)
        Frame cliffFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder cliffTile = new MapTileBuilder(cliffFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cliffTile);

        // index 4 -- sand with small pebbles scattered on top (passable)
        Frame pebblesOverlayFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder sandWithPebblesTile = new MapTileBuilder(sandFrame)
                .withTopLayer(pebblesOverlayFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(sandWithPebblesTile);

        // index 5 -- cactus standing on sand (not passable)
        Frame cactusOverlayFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder cactusTile = new MapTileBuilder(sandFrame)
                .withTopLayer(cactusOverlayFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cactusTile);

        // index 6 -- wind-rippled dune sand (passable)
        Frame duneFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder duneTile = new MapTileBuilder(duneFrame);

        mapTiles.add(duneTile);

        // index 7 -- cracked dry earth (passable)
        Frame crackedFrame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder crackedTile = new MapTileBuilder(crackedFrame);

        mapTiles.add(crackedTile);

        // index 8 -- bleached skull and bone lying on the sand (passable, decoration)
        Frame skullOverlayFrame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder skullTile = new MapTileBuilder(sandFrame)
                .withTopLayer(skullOverlayFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(skullTile);

        // ---------------- more desert scenery ----------------

        // index 9 -- tumbleweed on the sand (not passable)
        mapTiles.add(overlayTile(sandFrame, 0, 3, TileType.NOT_PASSABLE));

        // index 10 -- dead, dried-out bush (not passable)
        mapTiles.add(overlayTile(sandFrame, 0, 4, TileType.NOT_PASSABLE));

        // index 11 -- palm tree, for an oasis (not passable)
        mapTiles.add(overlayTile(sandFrame, 0, 5, TileType.NOT_PASSABLE));

        // index 12 -- oasis water (not passable)
        mapTiles.add(plainTile(1, 3, TileType.NOT_PASSABLE));

        // index 13 -- broken sandstone column, ruins decoration (not passable)
        mapTiles.add(overlayTile(sandFrame, 1, 4, TileType.NOT_PASSABLE));

        // ---------------- pyramid (outside) ----------------
        // build it in rows that get 2 tiles wider each step down:
        //        14
        //     15 16 17
        //  15 16 16 16 17
        //  15 16 18 16 17   <- 18 = the doorway, put it in the bottom row

        // index 14 -- pyramid capstone, the gold tip (not passable)
        mapTiles.add(plainTile(1, 5, TileType.NOT_PASSABLE));

        // index 15 -- pyramid left edge, sloped (not passable)
        mapTiles.add(plainTile(2, 3, TileType.NOT_PASSABLE));

        // index 16 -- pyramid brick wall (not passable)
        mapTiles.add(plainTile(2, 4, TileType.NOT_PASSABLE));

        // index 17 -- pyramid right edge, sloped (not passable)
        mapTiles.add(plainTile(2, 5, TileType.NOT_PASSABLE));

        // index 18 -- pyramid doorway (passable) -- walking into it is where the "enter the pyramid" map switch goes
        mapTiles.add(plainTile(3, 0, TileType.PASSABLE));

        // ---------------- pyramid (inside) ----------------

        // index 19 -- stone floor tiles (passable)
        Frame interiorFloorFrame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();
        mapTiles.add(new MapTileBuilder(interiorFloorFrame));

        // index 20 -- inside wall, plain bricks (not passable)
        mapTiles.add(plainTile(3, 2, TileType.NOT_PASSABLE));

        // index 21 -- inside wall with hieroglyphs (not passable)
        mapTiles.add(plainTile(3, 3, TileType.NOT_PASSABLE));

        // index 22 -- inside wall with a lit torch (not passable)
        mapTiles.add(plainTile(3, 4, TileType.NOT_PASSABLE));

        // index 23 -- clay urn standing on the stone floor (not passable)
        mapTiles.add(overlayTile(interiorFloorFrame, 3, 5, TileType.NOT_PASSABLE));

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