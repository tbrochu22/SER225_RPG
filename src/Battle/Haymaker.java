package Battle;
import java.awt.*;
public class Haymaker extends Move{
    public Haymaker(){
        super("Haymaker", 80, 0, 85, 2, "Charge Attack", "80 base power with 85% accuracy. Takes a turn to charge.");
    }
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawImpact(g, frame, targetX, targetY, new Color(255, 200, 40));   // big gold burst
    drawImpact(g, frame, targetX, targetY, Color.white);               // white center
}
}
