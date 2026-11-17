package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// A cocky little imp on the volcano map (ThirdMap) who challenges the player to a race.
// Imp.png is a single 24x24 frame facing right, so STAND_LEFT is the flipped one (same setup as Nomad)
public class Imp extends NPC {

    public Imp(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Imp.png"), 24, 24), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(7, 4, 11, 18)
                            .build()
            });
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(7, 4, 11, 18)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });
        }};
    }
}