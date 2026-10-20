package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

public class GlassEye extends EnhancedMapTile {

    public GlassEye(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("glasseye.png"), 24, 24), TileType.NOT_PASSABLE);
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0)).withScale(3).build();
        return new GameObject(x, y, frame);
    }
}