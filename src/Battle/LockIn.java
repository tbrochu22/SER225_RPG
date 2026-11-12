package Battle;
import java.awt.*;
public class LockIn extends Move {
    public LockIn() {
        super("Lock-In", 0, 0, 100, 3, "Lock", "Guarentees next move to hit.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = Math.min(1, progress(frame) / 0.5);      // shrinks during the first half
    int r = (int) (140 - 80 * p);
    setAlpha(g, fadeOut(frame));
    g.setColor(p >= 1 ? Color.blue : new Color(255, 220, 60));   // blue once locked on
    g.setStroke(new BasicStroke(4));
    g.drawOval(userX - r, userY - r, r * 2, r * 2);
    g.drawLine(userX - r - 15, userY, userX - r + 20, userY);
    g.drawLine(userX + r - 20, userY, userX + r + 15, userY);
    g.drawLine(userX, userY - r - 15, userX, userY - r + 20);
    g.drawLine(userX, userY + r - 20, userX, userY + r + 15);
}
}