package Battle;
import java.awt.*;
public class Slimeball extends Move{
    public Slimeball(){
        super("Slimeball", 30, 5, 90, 10, "Atk Lower Attack", "30 base power with 90% accuracy. Also lowers enemy's attack by 5.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = progress(frame);
    Color slime = new Color(90, 220, 70);
    if (p < 0.4) {
        // flying: straight line from user to target, minus a sine "hump" so it arcs
        double t = p / 0.4;
        int bx = (int) (userX + (targetX - userX) * t);
        int by = (int) (userY + (targetY - userY) * t - Math.sin(t * Math.PI) * 120);
        g.setColor(slime);
        g.fillOval(bx - 18, by - 18, 36, 36);
    } else {
        // splat: a flat blob plus droplets flying outward, all fading
        double t = (p - 0.4) / 0.6;
        setAlpha(g, 1 - t);
        g.setColor(slime);
        g.fillOval(targetX - 45, targetY - 25, 90, 50);
        for (int i = 0; i < 6; i++) {
            double a = Math.PI * 2 * i / 6;
            int d = (int) (30 + 50 * t);
            g.fillOval((int) (targetX + Math.cos(a) * d) - 8, (int) (targetY + Math.sin(a) * d) - 8, 16, 16);
        }
    }
}
}
