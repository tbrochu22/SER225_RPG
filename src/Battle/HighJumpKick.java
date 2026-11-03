package Battle;
import java.awt.*;

// Reward for the Mage's quest (defeat 3 roaming enemies).
// Hits hard, but if it misses the user crashes and takes "effect" damage instead.
public class HighJumpKick extends Move {
    public HighJumpKick() {
        super("High Jump Kick", 70, 15, 80, 3, "Crash Attack",
                "70 base power with 80% accuracy. If it misses, you crash and take 15 damage.");
    }
        @Override
    public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
        double p = progress(frame);

        // 0% -> 30%: a boot flies from the user to the target in a high arc
        if (p < 0.3) {
            double t = p / 0.3;                                   // 0 -> 1 along the arc
            int bx = (int) (userX + (targetX - userX) * t);
            int by = (int) (userY + (targetY - userY) * t - 120 * Math.sin(Math.PI * t)); // up, then down
            drawBoot(g, bx, by);
            return;
        }

        // 30% -> end: orange burst with a white center, plus the boot stuck on the target
        drawImpact(g, frame, targetX, targetY, new Color(255, 120, 30));
        setAlpha(g, fadeOut(frame));
        g.setColor(Color.white);
        g.fillOval(targetX - 30, targetY - 30, 60, 60);    // white flash in the middle
        drawBoot(g, targetX, targetY);
    }

    // a simple boot: a tall leg piece and a foot pointing right, centered on (x, y)
    private void drawBoot(Graphics2D g, int x, int y) {
        g.setColor(new Color(110, 70, 40));
        g.fillRoundRect(x - 14, y - 30, 22, 40, 8, 8);   // leg
        g.fillRoundRect(x - 14, y + 2, 42, 18, 12, 12);  // foot
        g.setColor(new Color(60, 35, 20));
        g.fillRect(x - 14, y + 16, 42, 5);               // sole
    }
}