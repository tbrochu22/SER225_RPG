package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

// A desert tileset (sand, sandstone, cactus) defined in the DesertTileset.png file
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

        return mapTiles;
    }
}