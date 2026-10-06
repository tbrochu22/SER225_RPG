package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

// A rocky, outdoor tileset (canyon/mountain path style) defined in the RockyTileset.png file
public class RockyTileset extends Tileset {

    public RockyTileset() {
        super(ImageLoader.load("RockyTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // index 0 -- rocky ground (base terrain, passable)
        Frame groundFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder groundTile = new MapTileBuilder(groundFrame);

        mapTiles.add(groundTile);

        // index 1 -- dirt path winding through the map (passable)
        Frame pathFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder pathTile = new MapTileBuilder(pathFrame);

        mapTiles.add(pathTile);

        // index 2 -- boulder (not passable)
        Frame boulderFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder boulderTile = new MapTileBuilder(boulderFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(boulderTile);

        // index 3 -- cliff wall, used to border the map (not passable)
        Frame cliffFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder cliffTile = new MapTileBuilder(cliffFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cliffTile);

        // index 4 -- rocky ground with small pebbles scattered on top (passable)
        Frame pebblesOverlayFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder groundWithPebblesTile = new MapTileBuilder(groundFrame)
                .withTopLayer(pebblesOverlayFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(groundWithPebblesTile);

        // index 5 -- dead shrub/scrub bush (not passable)
        Frame shrubFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder shrubTile = new MapTileBuilder(shrubFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(shrubTile);

        return mapTiles;
    }
}