package Battle;
import java.awt.*;
public class VigorUp extends Move {
    //5 uses
    public VigorUp() {
        super("Vigor Up", 0, 5, 100, 5, "Defense Buff", "Raises the user's defense by 5.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawArrows(g, frame, userX, userY, new Color(60, 140, 255), true);   // defense up
}
}