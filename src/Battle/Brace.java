package Battle;
import java.awt.*;
public class Brace extends Move {
    public Brace() {
        super("Brace", 0, 0, 100, 2, "Protect", "Protects the user from damage for one turn.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double grow = Math.min(1, progress(frame) / 0.3);   // bubble grows in the first 30%
    int r = (int) (95 * grow);
    setAlpha(g, 0.3 * fadeOut(frame));                  // see-through fill
    g.setColor(new Color(100, 180, 255));
    g.fillOval(userX - r, userY - r, r * 2, r * 2);
    setAlpha(g, fadeOut(frame));                        // solid outline
    g.setStroke(new BasicStroke(4));
    g.setColor(new Color(200, 230, 255));
    g.drawOval(userX - r, userY - r, r * 2, r * 2);
}
}