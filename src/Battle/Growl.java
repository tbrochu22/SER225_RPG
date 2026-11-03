package Battle;
import java.awt.*;
public class Growl extends Move{
    public Growl(){
        super("Growl", 0, 10, 80, 999, "Attack Debuff", "Lowers the opponent's attack by 10.");
    }    
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawRings(g, frame, userX, userY, new Color(120, 40, 160));            // purple sound waves
    drawArrows(g, frame, targetX, targetY, new Color(220, 40, 40), false); // their attack down
}
}
