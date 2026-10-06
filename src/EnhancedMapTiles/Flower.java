package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;

public class Flower extends EnhancedMapTile {

    private boolean triggered = false;
    private boolean used = false;

    public Flower(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Flower.png"), 16, 16), TileType.PASSABLE);
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0)).withScale(3).build();
        return new GameObject(x, y, frame);
    }

    @Override
    public void update(Player player) {
        super.update(player);
        if (!used && player.touching(this)) {
            triggered = true;
            used = true;
        }
    }

    public boolean consumeTrigger() {
        if (triggered) {
            triggered = false;
            return true;
        }
        return false;
    }
}
