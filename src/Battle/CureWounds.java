package Battle;
import java.awt.*;
public class CureWounds extends Move {
    public CureWounds() {
        //3 uses
        super("Cure Wounds", 0, 40, 100, 3, "Healing", "Restores 40 health points to the user.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    setAlpha(g, 0.35 * fadeOut(frame));                 // soft green glow
    g.setColor(new Color(80, 255, 120));
    g.fillOval(userX - 80, userY - 80, 160, 160);
    setAlpha(g, fadeOut(frame));
    g.setColor(new Color(60, 230, 90));
    for (int i = 0; i < 6; i++) {                       // plus signs floating up
        int px = userX - 70 + i * 28;
        int py = (int) (userY + 60 - 140 * ((progress(frame) + i * 0.17) % 1.0));
        g.fillRect(px - 3, py - 10, 6, 20);
        g.fillRect(px - 10, py - 3, 20, 6);
    }
}
}