
package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// A townsperson who just stands in one spot and talks when you interact with them.
// One class works for every villager: pass in which 24x24 sprite to use (drawn facing right, like Nomad.png)
// and which way they should face until the player talks to them.
public class Villager extends NPC {

    public Villager(int id, Point location, String spriteFile, Direction facing) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load(spriteFile), 24, 24),
                facing == Direction.LEFT ? "STAND_LEFT" : "STAND_RIGHT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(6, 4, 12, 18)
                            .build()
            });
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(6, 4, 12, 18)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });
        }};
    }
}
