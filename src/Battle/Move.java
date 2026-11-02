package Battle;
import java.awt.*;
public class Move {
    protected String name;
    protected int power;
    protected int effect;
    protected int accuracy;
    protected int uses;
    protected String type;
    protected String description;
    public static final int animationLength = 45;
    public static int getAnimationLength(){
        return animationLength;
    }
    protected double progress(int frame){
        return (double) frame/animationLength;
    }
    protected void setAlpha(Graphics2D g, double alpha){
        float a = (float) Math.max(0, Math.min(1, alpha));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
    }
    // 1.0 for the first 70%, then fades to 0.0 by the end
    protected double fadeOut(int frame) {
    double p = progress(frame);
    return p < 0.7 ? 1.0 : (1.0 - p) / 0.3;
    }
    public Move(String name, int power, int effect, int accuracy, int uses, String type, String description){
        this.name = name;
        this.power = power;
        this.effect = effect;
        this.accuracy = accuracy;
        this.uses = uses;
        this.type = type;
        this.description = description;
    }
    public void drawAnimation(Graphics2D g, int frame, int userX, int userY, int targetX, int targetY){
        double progress = (double) frame / animationLength;     // 0.0 -> 1.0
    int r = (int) (60 * progress);
    float alpha = (float) (1 - progress);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    g.setColor(Color.white);
    g.fillOval(targetX - r, targetY - r, r * 2, r * 2);
    g.setComposite(AlphaComposite.SrcOver); // reset, or the buttons come out see-through
    }
    // Two rows of teeth that close on (x, y) between 25% and 50%
    protected void drawTeeth(Graphics2D g, int frame, int x, int y, Color color) {
    double p = Math.max(0, Math.min(1, (progress(frame) - 0.25) / 0.25));
    int gap = (int) (60 * (1 - p));   // 60 apart -> 0 as p goes 0 -> 1
    setAlpha(g, fadeOut(frame));
    g.setColor(color);
    for (int i = 0; i < 4; i++) {
        int tx = x - 60 + i * 30;
        g.fillPolygon(new int[] {tx, tx + 30, tx + 15},             // top tooth, points down
                      new int[] {y - gap - 30, y - gap - 30, y - gap}, 3);
        g.fillPolygon(new int[] {tx, tx + 30, tx + 15},             // bottom tooth, points up
                      new int[] {y + gap + 30, y + gap + 30, y + gap}, 3);
    }
    }
        // Spiky starburst that grows on (x, y) from 25%, then fades
    protected void drawImpact(Graphics2D g, int frame, int x, int y, Color color) {
        double p = progress(frame);
        if (p < 0.25) return;
        double grow = Math.min(1, (p - 0.25) / 0.2);
        int outer = (int) (70 * grow), inner = outer / 2;
        int[] xs = new int[16], ys = new int[16];
        for (int i = 0; i < 16; i++) {
            double a = Math.PI * 2 * i / 16;
            int r = (i % 2 == 0) ? outer : inner;   // alternate long and short points
            xs[i] = (int) (x + Math.cos(a) * r);
            ys[i] = (int) (y + Math.sin(a) * r);
        }
        setAlpha(g, fadeOut(frame));
        g.setColor(color);
        g.fillPolygon(xs, ys, 16);
    }

    // Three arrows over (x, y) that float up (buff) or sink down (debuff)
    protected void drawArrows(Graphics2D g, int frame, int x, int y, Color color, boolean up) {
        setAlpha(g, fadeOut(frame));
        g.setColor(color);
        for (int i = 0; i < 3; i++) {
            int ax = x - 50 + i * 50;
            double t = (progress(frame) * 1.5 + i * 0.2) % 1.0;   // loops 0 -> 1
            int ay = up ? (int) (y + 50 - 100 * t) : (int) (y - 50 + 100 * t);
            int dir = up ? -1 : 1;                                // which way the tip points
            g.fillPolygon(new int[] {ax - 14, ax + 14, ax},
                          new int[] {ay, ay, ay + 18 * dir}, 3);
            g.fillRect(ax - 5, up ? ay : ay - 18, 10, 18);
        }
    }

    // Three rings that spread out from (x, y), like sound waves
    protected void drawRings(Graphics2D g, int frame, int x, int y, Color color) {
        setAlpha(g, fadeOut(frame));
        g.setColor(color);
        g.setStroke(new BasicStroke(5));
        for (int i = 0; i < 3; i++) {
            double q = progress(frame) * 1.5 - i * 0.25;   // each ring starts a bit later
            if (q <= 0 || q > 1) continue;
            int r = (int) (30 + 150 * q);
            g.drawOval(x - r, y - r, r * 2, r * 2);
        }
    }

    // Drawn on a charge-up turn (Haymaker's "is charging!"). Pulsing glow on the user.
    public void drawChargeAnimation(Graphics2D g, int frame, int userX, int userY) {
        int r = (int) (70 + 15 * Math.sin(frame * 0.4));   // pulses
        setAlpha(g, 0.4 * fadeOut(frame));
        g.setColor(new Color(255, 210, 40));
        g.fillOval(userX - r, userY - r, r * 2, r * 2);
        drawArrows(g, frame, userX, userY, new Color(255, 170, 0), true);
    }

    public String getName(){
        return name;
    }
    public int getPower(){
        return power;
    }
    public int getEffect(){
        return effect;
    }
    public int getAccuracy(){
        return accuracy;
    }
    public int getUses(){
        return uses;
    }
    public String getType(){
        return type;
    }
    public String getDescription(){
        return description;
    }
}
