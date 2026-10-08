package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

import Battle.Grab;
import Battle.Kick;
import Battle.Move;
import Battle.Punch;
import Battle.Tackle;

// This class is for the encounter screen
public class EncounterScreen extends Screen {
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
    protected SpriteFont playerLabel;
    protected SpriteFont enemyLabel;
    protected SpriteFont[] buttonLabels = new SpriteFont[4];
    protected Move[] moves = { new Tackle(), new Punch(), new Kick(), new Grab()};
    protected int textBoxTimer;
    protected SpriteFont moveInfoText;

    public EncounterScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    public void setMonsterName(String mName){
        initialize();
        enemyPlaceholder.setText(mName + " will go here");
    }
    @Override
    public void initialize() {
        battleCurrentMenuItemX = 0;
        battleCurrentMenuItemY = 0;
        textBoxTimer = 0;
        battleMenuItemSelected = -1;
        moveInfoText = new SpriteFont("...", 35, 405, "Arial", 22, Color.white);
        instructions = new SpriteFont("Press enter to return", 120, 279,"Arial", 20, Color.white);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.DOWN);
        keyLocker.lockKey(Key.LEFT);
        keyLocker.lockKey(Key.RIGHT);
        keyLocker.lockKey(Key.UP);
        keyLocker.lockKey(Key.SPACE);
        playerPlaceholder = new SpriteFont("Player will go here", 95, 400, "Arial", 24, Color.black);
        enemyPlaceholder = new SpriteFont("Enemy will go here", 480, 230, "Arial", 24, Color.black);
        playerLabel = new SpriteFont("Player", 10, ScreenManager.getScreenHeight() - 60, "Arial",24, Color.white);
        enemyLabel = new SpriteFont("Enemy", ScreenManager.getScreenWidth() - 85, 45, "Arial", 24, Color.white);
        enemyHp = 100;
        enemyMaxHp = 100;
        playerHp = 100;
        playerMaxHp = 100; 
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
        if(Keyboard.isKeyUp(Key.DOWN)){
            keyLocker.unlockKey(Key.DOWN);
        }
        if(Keyboard.isKeyDown(Key.DOWN) && !keyLocker.isKeyLocked(Key.DOWN)){
            //move cursor down here
            battleCurrentMenuItemY = 1;
            keyLocker.lockKey(Key.DOWN);
        }
        if(Keyboard.isKeyUp(Key.UP)){
            keyLocker.unlockKey(Key.UP);
        }
        if(Keyboard.isKeyDown(Key.UP) && !keyLocker.isKeyLocked(Key.UP)){
            //move cursor up here
            battleCurrentMenuItemY = 0;
            keyLocker.lockKey(Key.UP);
        }
        if(Keyboard.isKeyUp(Key.LEFT)){
            keyLocker.unlockKey(Key.LEFT);
        }
        if(Keyboard.isKeyDown(Key.LEFT) && !keyLocker.isKeyLocked(Key.LEFT)){
            //move cursor left here
            battleCurrentMenuItemX = 0;
            keyLocker.lockKey(Key.LEFT);
        }
        if(Keyboard.isKeyUp(Key.RIGHT)){
            keyLocker.unlockKey(Key.RIGHT);
        }
        if(Keyboard.isKeyDown(Key.RIGHT) && !keyLocker.isKeyLocked(Key.RIGHT)){
            //move cursor right here
            battleCurrentMenuItemX = 1;
            keyLocker.lockKey(Key.RIGHT);
        }
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }

        // if enter is pressed, return to the main screen
        if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
            playLevelScreen.returnFromEncounter();
        }
        if(Keyboard.isKeyUp(Key.SPACE)){
            keyLocker.unlockKey(Key.SPACE);
        }
        if(Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)){
            battleMenuItemSelected = (battleCurrentMenuItemY * 2 + battleCurrentMenuItemX);
            keyLocker.lockKey(Key.SPACE);
            moveInfoText.setText("Name: " + moves[battleMenuItemSelected].getName() + "\nType: " + moves[battleMenuItemSelected].getType() + "\nPower: " + moves[battleMenuItemSelected].getPower() + "\nAccuracy: " + moves[battleMenuItemSelected].getAccuracy());
            textBoxTimer = 240;
        }
        //to make the text box disappear after a set time
        if(textBoxTimer > 0){
                textBoxTimer --;
                if(textBoxTimer == 0){
                battleMenuItemSelected = -1;
                }
            }
    }

    public void draw(GraphicsHandler graphicsHandler) {
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
        playerPlaceholder.draw(graphicsHandler);
        enemyPlaceholder.draw(graphicsHandler);
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
        if(battleMenuItemSelected != -1){
            graphicsHandler.drawFilledRectangleWithBorder(20, 395, 400, 125, new Color(30,30,60), Color.white, 3);
            moveInfoText.drawWithParsedNewLines(graphicsHandler, 6);
        }
        //instructions.draw(graphicsHandler);
    }
}
