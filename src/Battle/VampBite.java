package Battle;
import java.awt.*;
public class VampBite extends Move{
    public VampBite(){
        super("Vamp Bite", 40, 0, 90, 999, "Vamp", "40 base power with 90% accuracy and lifesteal.");
    }
    @Override 
    public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY) {
        drawTeeth(g, frame, targetX, targetY, (new Color(200, 0, 40)));
    }
}