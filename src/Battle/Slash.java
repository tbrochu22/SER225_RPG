package Battle;
import java.awt.*;
public class Slash extends Move {
    public Slash() {
        //15 uses
        super("Slash", 30, 0, 100, 15, "Attack", "30 base power with 100% accuracy.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = progress(frame);
    if (p < 0.25) return;                        // wait for the lunge to connect
    double swipe = Math.min(1, (p - 0.25) / 0.2); // draw the line between 25% and 45%
    setAlpha(g, p < 0.7 ? 1 : (1 - p) / 0.3);     // fade out over the last 30%

    int x1 = targetX - 80, y1 = targetY - 80;
    int x2 = (int) (x1 + 160 * swipe), y2 = (int) (y1 + 160 * swipe);
    g.setColor(Color.white);
    g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g.drawLine(x1, y1, x2, y2);
}
}