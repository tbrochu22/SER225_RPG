package Battle;
import java.awt.*;
public class Scratch extends Move{
    public Scratch(){
        super("Scratch", 40, 0, 95, 999, "Normal", "40 base power with 95% accuracy.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    setAlpha(g, fadeOut(frame));
    g.setColor(Color.white);
    g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    for (int i = 0; i < 3; i++) {
        double start = 0.25 + i * 0.1;   // each mark starts 10% after the last
        double p = Math.max(0, Math.min(1, (progress(frame) - start) / 0.2));
        if (p <= 0) continue;            // this mark hasn't started yet
        int sx = targetX - 50 + i * 30;
        int sy = targetY - 60;
        g.drawLine(sx, sy, (int) (sx + 30 * p), (int) (sy + 110 * p));
    }
}
}