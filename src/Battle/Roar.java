package Battle;
import java.awt.*;
public class Roar extends Move{
    public Roar(){
        super("Roar", 0, 5, 100, 999, "Attack Buff", "Raises the user's attack by 5.");
    }    
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawRings(g, frame, userX, userY, new Color(255, 140, 0));         // orange sound waves
    drawArrows(g, frame, userX, userY, new Color(255, 80, 40), true);  // attack up
}
}
