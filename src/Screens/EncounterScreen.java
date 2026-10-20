package Screens;

import Battle.Recover;
import Battle.Vigor;
import Battle.Move;
import Battle.Punch;
import Battle.Scratch;
import Battle.Tackle;
import Battle.electricBite;
import Battle.Bite;
import Battle.Roar;
import Engine.*;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;

// This class is for the encounter screen
public class EncounterScreen extends Screen {
    private static final int RESULT_SCREEN_DELAY = 3 * Config.TARGET_FPS;

    protected SpriteFont encounterMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;
    protected SpriteFont moveList;
    protected int movePointerLocationX, movePointerLocationY;
    protected int battleCurrentMenuItemX = 0;
    protected int battleCurrentMenuItemY = 0;
    protected int battleMenuItemSelected = -1;
    protected int battleKeyPressTimer;
    protected SpriteFont playerPlaceholder;
    protected SpriteFont enemyPlaceholder;
    protected int enemyHp, enemyMaxHp, playerHp, playerMaxHp;
    protected int damageRoll = 0;
    protected int playerDefenseBuff = 0;
    protected int enemyDefenseBuff = 0;
    protected int playerAttackDebuff = 0;
    protected int enemyAttackDebuff = 0;
    protected int resultScreenTimer;
    protected SpriteFont playerLabel;
    protected SpriteFont enemyLabel;
    protected SpriteFont[] buttonLabels = new SpriteFont[4];
    protected Move[] moves = { new Tackle(), new Punch(), new Vigor(), new Recover()};
    protected Move[] enemyMoves = { new Roar(), new Bite(), new electricBite(), new Scratch()};
    protected int textBoxTimer;
    protected int enemyTurnDelay;
    protected boolean enemyStarts;
    protected String battleMessage;
    protected SpriteFont moveInfoText;
    protected Random random = new Random();
    protected BufferedImage knightBack;
    protected BufferedImage enemySprite;

    public EncounterScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
        knightBack = ImageLoader.load("KnightBack.png");
    }

    public void setMonsterName(String mName){
        initialize();
        enemyPlaceholder.setText(mName + " will go here");
    }

    private String useMove(Move move, boolean playerAttacking) {
        if (random.nextInt(100) >= move.getAccuracy()) {
            return move.getName() + " missed!";
        }

        if (playerAttacking) {
            if (move.getPower() > 0) {
                damageRoll = (int)(Math.random() * 20) - 10;
                enemyHp = Math.max(0, enemyHp - ((move.getPower() + damageRoll) - playerAttackDebuff));
            } else {
                if (move.getType().equals("Defense Buff")) {
                    playerDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Healing")) {
                    playerHp = Math.min(playerMaxHp, playerHp + move.getEffect());
                }
            }
        } else {
            if (move.getPower() > 0) {
                damageRoll = (int)(Math.random() * 20) - 10;
                playerHp = Math.max(0, playerHp - ((move.getPower() + damageRoll) - playerDefenseBuff));
            } else {
                if (move.getType().equals("Defense Buff")) {
                    enemyDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Healing")) {
                    enemyHp = Math.min(enemyMaxHp, enemyHp + move.getEffect());
                } else if (move.getType().equals("Attack Debuff")) {
                    playerAttackDebuff += Math.abs(move.getEffect());
                }
            }
        }

        if (move.getPower() > 0 && playerAttacking) {
            return move.getName() + " hit for " + ((move.getPower() + damageRoll)) + " damage!";
        }

        if (move.getPower() > 0 && !playerAttacking) {
            if (((move.getPower() + damageRoll) - playerDefenseBuff) < 0) {
                return move.getName() + " hit for 1 damage!";
            }

            return move.getName() + " hit for " + ((move.getPower() + damageRoll) - playerDefenseBuff) + " damage!";
        }

        if (move.getType().equals("Defense Buff")) {
            return move.getName() + " increased your defense by " + move.getEffect() + "!";
        }
        if (move.getType().equals("Healing")) {
            return move.getName() + " restored " + Math.abs(move.getEffect()) + " HP!";
        }
        if (move.getType().equals("Attack Debuff")) {
            return move.getName() + " decreased your attack by " + Math.abs(move.getEffect()) + "!";
        }
        return move.getName() + " had no effect!";
    }

    @Override
    public void initialize() {
        battleCurrentMenuItemX = 0;
        battleCurrentMenuItemY = 0;
        enemyStarts = random.nextBoolean();
        enemyTurnDelay = enemyStarts ? 60 : 0;
        battleMessage = enemyStarts ? "Enemy moves first!" : "";
        textBoxTimer = enemyStarts ? 60 : 0;
        battleMenuItemSelected = enemyStarts ? 0 : -1;
        moveInfoText = new SpriteFont("...", 35, 405, "Arial", 22, Color.white);
        instructions = new SpriteFont("Press enter to return", 120, 279,"Arial", 20, Color.white);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.S);
        keyLocker.lockKey(Key.A);
        keyLocker.lockKey(Key.D);
        keyLocker.lockKey(Key.W);
        keyLocker.lockKey(Key.SPACE);
        playerPlaceholder = new SpriteFont("Player will go here", 95, 400, "Arial", 24, Color.black);
        enemyPlaceholder = new SpriteFont("Enemy will go here", 480, 230, "Arial", 24, Color.black);
        playerLabel = new SpriteFont("Player", 10, ScreenManager.getScreenHeight() - 60, "Arial",24, Color.white);
        enemyLabel = new SpriteFont("Enemy", ScreenManager.getScreenWidth() - 85, 45, "Arial", 24, Color.white);
        enemyHp = 100;
        enemyMaxHp = 100;
        playerHp = 100;
        playerMaxHp = 100; 
        resultScreenTimer = 0;
        for(int row = 0; row < 2; row ++){
            for(int col = 0; col < 2; col ++){
                int bx = 440 + col * 160;
                int by = 395 + row * 70;
                SpriteFont label = new SpriteFont(moves[row * 2 + col].getName(), bx + 30, by + 15, "Arial", 28, Color.white);
                label.setFontStyle(Font.BOLD);
                label.setOutlineColor(Color.blue);
                label.setOutlineThickness(3);
                buttonLabels[row * 2 + col] = label;
            }
        }
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (enemyHp <= 0 || playerHp <= 0) {
            if (resultScreenTimer > 0) {
                keyLocker.lockKey(Key.ENTER);
                resultScreenTimer--;
                return;
            }
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                keyLocker.lockKey(Key.ENTER);
                playLevelScreen.returnFromEncounter(playerHp <= 0);
            }
            return;
        }

        if (enemyTurnDelay > 0) {
            enemyTurnDelay--;
            if (enemyTurnDelay == 0 && enemyHp > 0 && playerHp > 0) {
                Move enemyMove = enemyMoves[random.nextInt(enemyMoves.length)];
                battleMessage += "\n" + useMove(enemyMove, false);
                moveInfoText.setText(battleMessage);
                textBoxTimer = 240;
                startResultScreenDelay();
                if (playerHp <= 0) {
                    return;
                }
            }
        }

        if(Keyboard.isKeyUp(Key.S)){
            keyLocker.unlockKey(Key.S);
        }
        if(Keyboard.isKeyDown(Key.S) && !keyLocker.isKeyLocked(Key.S)){
            //move cursor down here
            battleCurrentMenuItemY = 1;
            keyLocker.lockKey(Key.S);
        }
        if(Keyboard.isKeyUp(Key.W)){
            keyLocker.unlockKey(Key.W);
        }
        if(Keyboard.isKeyDown(Key.W) && !keyLocker.isKeyLocked(Key.W)){
            //move cursor up here
            battleCurrentMenuItemY = 0;
            keyLocker.lockKey(Key.W);
        }
        if(Keyboard.isKeyUp(Key.A)){
            keyLocker.unlockKey(Key.A);
        }
        if(Keyboard.isKeyDown(Key.A) && !keyLocker.isKeyLocked(Key.A)){
            //move cursor left here
            battleCurrentMenuItemX = 0;
            keyLocker.lockKey(Key.A);
        }
        if(Keyboard.isKeyUp(Key.D)){
            keyLocker.unlockKey(Key.D);
        }
        if(Keyboard.isKeyDown(Key.D) && !keyLocker.isKeyLocked(Key.D)){
            //move cursor right here
            battleCurrentMenuItemX = 1;
            keyLocker.lockKey(Key.D);
        }

        if(Keyboard.isKeyUp(Key.SPACE)){
            keyLocker.unlockKey(Key.SPACE);
        }
        if(Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)){
            keyLocker.lockKey(Key.SPACE);
            if (enemyTurnDelay == 0 && enemyHp > 0 && playerHp > 0) {
                battleMenuItemSelected = battleCurrentMenuItemY * 2 + battleCurrentMenuItemX;

                battleMessage = useMove(moves[battleMenuItemSelected], true);
                if (enemyHp > 0) {
                    enemyTurnDelay = 60;
                }

                moveInfoText.setText(battleMessage);
                textBoxTimer = 240;
                startResultScreenDelay();
                if (enemyHp <= 0) {
                    return;
                }
            }
        }
        //to make the text box disappear after a set time
        if(textBoxTimer > 0){
                textBoxTimer --;
                if(textBoxTimer == 0){
                battleMenuItemSelected = -1;
                }
            }
    }

    private void startResultScreenDelay() {
        if (enemyHp <= 0 || playerHp <= 0) {
            resultScreenTimer = RESULT_SCREEN_DELAY;
            battleMessage = enemyHp <= 0
                    ? "The enemy has been defeated!"
                    : "The player has been defeated!";
            moveInfoText.setText(battleMessage);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (enemyHp <= 0 && resultScreenTimer <= 0) {
            drawVictoryScreen(graphicsHandler);
            return;
        }

        if (playerHp <= 0 && resultScreenTimer <= 0) {
            drawLoseScreen(graphicsHandler);
            return;
        }

        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.blue);
       Graphics2D g = graphicsHandler.getGraphics();
        g.setColor(new Color(250, 230, 90));
        g.fillOval(40, 40, 80, 80);  
        g.setColor(new Color(100, 200, 100));
        g.fillOval(-350, 150, 1000, 700);
        g.setColor(new Color(125, 225, 100));
        g.fillOval(200,150,1000,500);
        g.setColor(new Color(170, 250, 100)); 
        g.fillOval(-100,150,1000,600);
        g.setColor(Color.black);
        g.fillOval(40, 420, 310, 120);       // outer black ring
        g.setColor(new Color(80, 80, 80));
        g.fillOval(110, 445, 170, 60);       // inner gray part
        g.setColor(Color.black);
        g.fillOval(450, 260, 300, 100);
        g.setColor(Color.gray);
        g.fillOval( 510, 275, 160, 50);
        int barWidth = ScreenManager.getScreenWidth() - 20;
        int enemyFill = (int) (barWidth * ((double) enemyHp / enemyMaxHp));
        int barHeight = ScreenManager.getScreenHeight() - 24;
        graphicsHandler.drawFilledRectangleWithBorder(10, 10, barWidth, 14, Color.darkGray, Color.black, 2);
        graphicsHandler.drawFilledRectangle(10, 10, enemyFill, 14, new Color(120, 230, 70));
        int playerFill = (int) (barWidth * ((double) playerHp / playerMaxHp));
        graphicsHandler.drawFilledRectangleWithBorder(10, barHeight, barWidth, 14, Color.darkGray, Color.black, 2);
        graphicsHandler.drawFilledRectangle(10, barHeight, playerFill, 14, new Color(120, 230, 70));
        playerLabel.draw(graphicsHandler);
        enemyLabel.draw(graphicsHandler);
        //playerPlaceholder.draw(graphicsHandler);
        graphicsHandler.drawImage(knightBack, 115, 315, 160, 160);
        if(enemySprite != null){
        graphicsHandler.drawImage(enemySprite, 500, 100, 192, 192);
        }else{
        enemyPlaceholder.draw(graphicsHandler);
        }
        //for the buttons
        for(int row = 0; row < 2; row ++){
            for(int col = 0; col < 2; col ++){
                int bx = 440 + col * 160;
                int by = 395 + row * 70;
                Color borderColor = Color.black;
                if(battleCurrentMenuItemX == col && battleCurrentMenuItemY == row){
                    borderColor = Color.yellow;
                }
                graphicsHandler.drawFilledRectangleWithBorder(bx, by, 145, 55, new Color(200, 50, 40), borderColor, 3);
                buttonLabels[row * 2 + col].draw(graphicsHandler);
            }
        }
        if(battleMenuItemSelected != -1 || resultScreenTimer > 0){
            graphicsHandler.drawFilledRectangleWithBorder(20, 395, 400, 125, new Color(30,30,60), Color.white, 3);
            moveInfoText.drawWithParsedNewLines(graphicsHandler, 6);
        }
        //instructions.draw(graphicsHandler);
    }

    private void drawVictoryScreen(GraphicsHandler graphicsHandler) {
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();
        graphicsHandler.drawFilledRectangle(0, 0, screenWidth, screenHeight, Color.black);

        Graphics2D graphics = graphicsHandler.getGraphics();
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        Font promptFont = new Font("Arial", Font.PLAIN, 24);
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        FontMetrics promptMetrics = graphics.getFontMetrics(promptFont);

        String title = "You Win";
        String prompt = "Press Enter to return to overworld";
        graphicsHandler.drawString(title, (screenWidth - titleMetrics.stringWidth(title)) / 2,
                screenHeight / 2, titleFont, Color.green);
        graphicsHandler.drawString(prompt, (screenWidth - promptMetrics.stringWidth(prompt)) / 2,
                screenHeight / 2 + 50, promptFont, Color.white);
    }

    private void drawLoseScreen(GraphicsHandler graphicsHandler) {
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();
        graphicsHandler.drawFilledRectangle(0, 0, screenWidth, screenHeight, Color.black);

        Graphics2D graphics = graphicsHandler.getGraphics();
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        Font promptFont = new Font("Arial", Font.PLAIN, 24);
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        FontMetrics promptMetrics = graphics.getFontMetrics(promptFont);

        String title = "You Lose";
        String prompt = "Press Enter to return to overworld";
        graphicsHandler.drawString(title, (screenWidth - titleMetrics.stringWidth(title)) / 2,
                screenHeight / 2, titleFont, Color.red);
        graphicsHandler.drawString(prompt, (screenWidth - promptMetrics.stringWidth(prompt)) / 2,
                screenHeight / 2 + 50, promptFont, Color.white);
    }
}