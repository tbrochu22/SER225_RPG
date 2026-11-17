
package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;
import java.util.Random;

// A small animal that patrols back and forth on its own, like Bug, but stops for a moment at each end
// so it looks like it's sniffing around instead of pacing like a robot.
// The sprite sheet is two 24x16 walk frames side by side with a 1px gap (same layout as Bug.png), drawn facing right.
public class Critter extends NPC {
    private final int patrolDistance;      // how many pixels to walk before turning around
    private final float speed = 1;
    private final Random random = new Random();

    private Direction direction = Direction.RIGHT;
    private int amountMoved = 0;
    private int pauseTimer = 0;            // frames left to stand still

    public Critter(int id, Point location, String spriteFile, int patrolDistance) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load(spriteFile), 24, 16), "WALK_RIGHT");
        this.patrolDistance = patrolDistance;
    }

    @Override
    public void performAction(Player player) {
        // standing still for a bit at the end of a lap
        if (pauseTimer > 0) {
            pauseTimer--;
            currentAnimationName = direction == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
            return;
        }

        float moved = moveXHandleCollision(speed * direction.getVelocity());
        amountMoved += Math.abs(moved);

        // turn around after walking far enough, or right away if something is in the way (moved == 0)
        if (amountMoved >= patrolDistance || moved == 0) {
            amountMoved = 0;
            direction = direction == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
            pauseTimer = 40 + random.nextInt(80);   // about 0.7 to 2 seconds at 60fps
        }

        currentAnimationName = direction == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] { frame(spriteSheet, 0, false, 0) });
            put("STAND_LEFT", new Frame[] { frame(spriteSheet, 0, true, 0) });
            put("WALK_RIGHT", new Frame[] { frame(spriteSheet, 0, false, 10), frame(spriteSheet, 1, false, 10) });
            put("WALK_LEFT", new Frame[] { frame(spriteSheet, 0, true, 10), frame(spriteSheet, 1, true, 10) });
        }};
    }

    private Frame frame(SpriteSheet spriteSheet, int column, boolean flip, int delay) {
        FrameBuilder builder = delay > 0
                ? new FrameBuilder(spriteSheet.getSprite(0, column), delay)
                : new FrameBuilder(spriteSheet.getSprite(0, column));
        builder.withScale(2).withBounds(2, 6, 20, 9);
        if (flip) {
            builder.withImageEffect(ImageEffect.FLIP_HORIZONTAL);
        }
        return builder.build();
    }
}
