package Battle;
import java.awt.*;
public class OmniChant extends Move{
    public OmniChant(){
        super("Omni-Chant", 0, 15, 100, 999, "Omni", "Sacrifices 30% ofthe user's health to raise their attack and defense by 15 each.");
    }    
    @Override
public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
    drawRings(g, frame, userX, userY, new Color(128, 0, 128));         // purple sound waves
    drawArrows(g, frame, userX, userY, new Color(147, 112, 219), true);  // attack up
}
}
