package Battle;
import java.awt.*;

public class Bite extends Move {
    public Bite() {
        super("Bite", 40, 0, 95, 999, "Normal", "40 base power with 95% accuracy.");
    }

    @Override
    public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
        drawTeeth(g, frame, targetX, targetY, Color.white);
    }
}