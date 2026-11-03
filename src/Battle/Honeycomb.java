package Battle;
import java.awt.*;
public class Honeycomb extends Move{
    public Honeycomb(){
        super("Honeycomb", 40, 5, 95, 999, "Defense Boost Attack", "40 base power with 95% accuracy. Also raises user's defense.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = Math.min(1, progress(frame) / 0.4);   // hexagons fly during the first 40%
    setAlpha(g, fadeOut(frame));
    for (int i = 0; i < 3; i++) {
        double t = Math.max(0, Math.min(1, p - i * 0.1));
        int hx = (int) (userX + (targetX - userX) * t) + (i - 1) * 25;
        int hy = (int) (userY + (targetY - userY) * t) + (i - 1) * 15;
        fillHexagon(g, hx, hy, 18, new Color(255, 190, 30));
    }
    if (progress(frame) > 0.4) {                     // then a honey glow on the user
        setAlpha(g, 0.4 * fadeOut(frame));
        fillHexagon(g, userX, userY, 90, new Color(255, 200, 60));
    }
}

private void fillHexagon(Graphics2D g, int x, int y, int r, Color color) {
    int[] xs = new int[6], ys = new int[6];
    for (int i = 0; i < 6; i++) {
        double a = Math.PI / 3 * i;
        xs[i] = (int) (x + Math.cos(a) * r);
        ys[i] = (int) (y + Math.sin(a) * r);
    }
    g.setColor(color);
    g.fillPolygon(xs, ys, 6);
    g.setColor(new Color(150, 90, 0));
    g.setStroke(new BasicStroke(2));
    g.drawPolygon(xs, ys, 6);
}
}
