package Battle;
import java.awt.*;
public class Hawkeye extends Move {
    public Hawkeye() {
        super("Hawkeye", 0, 0, 100, 3, "Stun", "50% chance to stun the opponent each turn.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = Math.min(1, progress(frame) / 0.5);      // shrinks during the first half
    int r = (int) (140 - 80 * p);
    setAlpha(g, fadeOut(frame));
    g.setColor(p >= 1 ? Color.red : new Color(255, 220, 60));   // red once locked on
    g.setStroke(new BasicStroke(4));
    g.drawOval(targetX - r, targetY - r, r * 2, r * 2);
    g.drawLine(targetX - r - 15, targetY, targetX - r + 20, targetY);
    g.drawLine(targetX + r - 20, targetY, targetX + r + 15, targetY);
    g.drawLine(targetX, targetY - r - 15, targetX, targetY - r + 20);
    g.drawLine(targetX, targetY + r - 20, targetX, targetY + r + 15);
}
}