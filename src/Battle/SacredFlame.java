package Battle;
import java.awt.*;
public class SacredFlame extends Move {
    public SacredFlame() {
        super("Sacred Flame", 0, 0, 100, 5, "Burn", "Inflicts a burn status on the target, taking damage each turn.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    double p = progress(frame);
    setAlpha(g, fadeOut(frame));
    Color[] colors = {new Color(255, 80, 0), new Color(255, 170, 0), new Color(255, 240, 120)};
    for (int i = 0; i < 5; i++) {                       // 5 flames along the target's feet
        int fx = targetX - 60 + i * 30;
        int height = (int) ((50 + 30 * Math.sin(frame * 0.5 + i)) * Math.min(1, p / 0.3));
        for (int c = 0; c < colors.length; c++) {       // 3 layers, each smaller
            int w = 26 - c * 8, h = height - c * 15;
            if (h <= 0) continue;
            g.setColor(colors[c]);
            g.fillPolygon(new int[] {fx - w / 2, fx + w / 2, fx},
                          new int[] {targetY + 70, targetY + 70, targetY + 70 - h}, 3);
        }
    }
}
}