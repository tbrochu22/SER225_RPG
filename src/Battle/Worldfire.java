package Battle;
import java.awt.*;
public class Worldfire extends Move {
    public Worldfire() {
        super("Worldfire", 1, 0, 100, 2, "Burn Attack", "Halves the target's current health and inflicts a burn status.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
     double p = progress(frame);
    setAlpha(g, 0.9 * (1 - p));                         // whole-screen white flash
    g.setColor(Color.red);
    g.fillRect(0, 0, 800, 605);
    setAlpha(g, fadeOut(frame));                        // rays bursting out of the target
    g.setColor(Color.orange);
    g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    for (int i = 0; i < 12; i++) {
        double a = Math.PI * 2 * i / 12;
        int r1 = (int) (20 + 100 * p), r2 = r1 + 40;
        g.drawLine((int) (targetX + Math.cos(a) * r1), (int) (targetY + Math.sin(a) * r1),
                   (int) (targetX + Math.cos(a) * r2), (int) (targetY + Math.sin(a) * r2));
    }
}
}