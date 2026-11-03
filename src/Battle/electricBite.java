package Battle;
import java.awt.*;
public class electricBite extends Move{
    public electricBite(){
    super("Electric Bite", 40, 0, 80, 999, "Electric Attack", "40 base power attack with 80% accuracy and stun chance.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawTeeth(g, frame, targetX, targetY, Color.white);
    if (progress(frame) < 0.5 || frame % 4 >= 2) return;   // after the bite, flicker on/off
    setAlpha(g, fadeOut(frame));
    g.setColor(new Color(255, 240, 60));
    g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    for (int b = 0; b < 3; b++) {                 // 3 bolts
        double angle = b * 2.1 + frame * 0.3;     // spread out, and slowly spinning
        int px = targetX, py = targetY;
        for (int seg = 0; seg < 4; seg++) {       // 4 zig-zag segments per bolt
            int nx = (int) (targetX + Math.cos(angle) * 25 * (seg + 1)) + (seg % 2 == 0 ? 10 : -10);
            int ny = (int) (targetY + Math.sin(angle) * 25 * (seg + 1));
            g.drawLine(px, py, nx, ny);
            px = nx;
            py = ny;
        }
    }
}
}
