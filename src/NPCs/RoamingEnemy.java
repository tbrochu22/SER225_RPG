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

public class RoamingEnemy extends NPC{
    private final String enemyName;
    private final Random random = new Random();
    private Direction direction = Direction.DOWN;
    private float speed = 1;
    private int stepsLeft = 0;
    private int pauseTimer = 0;
    private static final int CHASE_RANGE = 150;     // pixels, about 3 tiles
    private static final float CHASE_SPEED = 1.5f;  // a bit faster than wandering, still slower than the knight
    
    private void pickNewPlan(){
        if(random.nextInt(3) == 0){
            pauseTimer = 60 + random.nextInt(60);
        }else{
            Direction[] choices = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
            direction = choices[random.nextInt(choices.length)];
            stepsLeft = 30 + random.nextInt(60);
        }
    }
    @Override 
    protected void performAction(Player player){
        if (isPlayerClose(player)) {
            chase(player);
            return;
        }
        if(pauseTimer > 0){
            pauseTimer --;
            stand(direction == Direction.RIGHT ? Direction.RIGHT: Direction.LEFT);
            return;
        }
        if(stepsLeft <= 0){
            pickNewPlan();
            return;
        }
        float moved;
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
        moved = moveXHandleCollision(speed * direction.getVelocity());
        } else {
        moved = moveYHandleCollision(speed * direction.getVelocity());
        }
        stepsLeft--;
        if(moved == 0){
            stepsLeft = 0;
        }
    }

    private void walkAnimation(Direction direction){
        if(direction == Direction.LEFT){
            currentAnimationName = "WALK_LEFT";
        } else if(direction == Direction.RIGHT){
            currentAnimationName = "WALK_RIGHT";
        } else{
            currentAnimationName = currentAnimationName.contains("RIGHT") ? "WALK_RIGHT" : "WALK_LEFT";
        }
    }
    private boolean isPlayerClose(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return Math.sqrt(dx * dx + dy * dy) < CHASE_RANGE;
    }

    private void chase(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx < 0 ? Direction.LEFT : Direction.RIGHT;
            moveXHandleCollision(CHASE_SPEED * direction.getVelocity());
        } else {
            direction = dy < 0 ? Direction.UP : Direction.DOWN;
            moveYHandleCollision(CHASE_SPEED * direction.getVelocity());
        }
        walkAnimation(direction);
    }
    public RoamingEnemy(int id, Point location, String spriteFile, String enemyName){
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load(spriteFile), 32, 32), "STAND_LEFT");
        this.enemyName = enemyName;
    }
    public String getEnemyName(){
        return enemyName;
    }
    @Override 
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet){
        return new HashMap<String, Frame[]>(){{
            put("STAND_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(6, 10, 20, 20)
                    .build()
            });
            put("STAND_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(6, 10, 20, 20)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("WALK_LEFT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(6, 10, 20, 20)
                    .build()
            });
            put("WALK_RIGHT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(6, 10, 20, 20)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
        }};
}
}