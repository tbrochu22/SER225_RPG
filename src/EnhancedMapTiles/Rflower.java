package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;

// Represents a flower that disappears when the player walks over it
public class Rflower extends EnhancedMapTile {

    private boolean visible = true;

    public Rflower(Point location) {
        super(location.x,location.y,new SpriteSheet(ImageLoader.load("Rflower.png"), 16, 16),TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        // If the player touches the flower, make it disappear
         if (visible && player.touching(this)) {
            visible = false;
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (visible) {
            super.draw(graphicsHandler);
        }
    }

    @Override
    public void drawBottomLayer(GraphicsHandler graphicsHandler) {
        if (visible) {
            super.drawBottomLayer(graphicsHandler);
        }
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0)).withScale(3).build();
        return new GameObject(x, y, frame);
    }
}