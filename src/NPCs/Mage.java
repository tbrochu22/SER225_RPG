package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// This class is for the mage npc, placed at the start of the rocky path to greet/introduce the player
public class Mage extends NPC {

    public Mage(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Mage.png"), 24, 24), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(4, 3, 17, 20)
                            .build()
            });
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(4, 3, 17, 20)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}