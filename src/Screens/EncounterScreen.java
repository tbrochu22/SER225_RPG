package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the encounter screen
public class EncounterScreen extends Screen {
    protected SpriteFont encounterMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public EncounterScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    public void setMonsterName(String mName){
        encounterMessage = new SpriteFont("You encountered " + mName, 120, 250, "Arial",20, Color.white);
    }
    @Override
    public void initialize() {
        instructions = new SpriteFont("Press enter to return", 120, 279,"Arial", 20, Color.white);
        keyLocker.lockKey(Key.ENTER);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }

        // if enter is pressed, return to the main screen
        if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
            playLevelScreen.returnFromEncounter();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        encounterMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
}
