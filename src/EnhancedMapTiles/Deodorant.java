package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

public class Deodorant extends EnhancedMapTile {

    public Deodorant(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("deodorant.png"), 16, 16), TileType.NOT_PASSABLE);
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0)).withScale(3).build();
        return new GameObject(x, y, frame);
    }
}