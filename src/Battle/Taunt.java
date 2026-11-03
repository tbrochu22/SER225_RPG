package Battle;
import java.awt.*;
public class Taunt extends Move {
    public Taunt() {
        super("Taunt", 0, 5, 100, 5, "Defense Debuff", "Lowers the target's defense by 5.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    // a throbbing red "anger mark" up and to the right of the target
    setAlpha(g, fadeOut(frame));
    g.setColor(new Color(230, 30, 30));
    g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    int s = (int) (12 + 8 * Math.sin(progress(frame) * Math.PI * 6));
    int mx = targetX + 55, my = targetY - 70;
    g.drawLine(mx - s, my - s / 3, mx - s / 3, my - s / 3);
    g.drawLine(mx - s / 3, my - s / 3, mx - s / 3, my - s);
    g.drawLine(mx + s, my + s / 3, mx + s / 3, my + s / 3);
    g.drawLine(mx + s / 3, my + s / 3, mx + s / 3, my + s);
    drawArrows(g, frame, targetX, targetY, new Color(60, 140, 255), false);   // defense down
}
}