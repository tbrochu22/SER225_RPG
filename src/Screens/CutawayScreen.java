package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is a placeholder for the "cutaway to fight" screen that plays when the player
// talks to a boss npc and triggers a fight. The actual fight sequence hasn't been built yet,
// so for now this just shows a black screen with a message saying so.
public class CutawayScreen extends Screen {
    protected SpriteFont cutawayMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public CutawayScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        cutawayMessage = new SpriteFont("Cutaway not created", 220, 239, "Arial", 30, Color.white);
        instructions = new SpriteFont("Press Escape to return to the game", 220, 279, "Arial", 20, Color.white);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if escape is pressed, return to normal gameplay
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            keyLocker.lockKey(Key.ESC);
            playLevelScreen.endCutaway();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        cutawayMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
}