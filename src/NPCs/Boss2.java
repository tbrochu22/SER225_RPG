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

// This class is for the second boss, a red octopus that waits at the end of the desert path (SecondMap)
// Talking to it kicks off Boss2Script, which leads into the boss fight
// Boss2.png is 24x24 like the other npcs, drawn at scale 3
public class Boss2 extends NPC {

    public Boss2(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Boss2.png"), 24, 24), "STAND_LEFT"); // faces left, toward the path
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