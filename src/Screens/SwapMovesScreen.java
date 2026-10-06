package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

public class SwapMovesScreen extends Screen {
    protected SpriteFont placeholderMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public SwapMovesScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        placeholderMessage = new SpriteFont("Swap Moves not created", 220, 239, "Arial", 30, Color.white);
        instructions = new SpriteFont("Press Escape to return to the game", 220, 279, "Arial", 20, Color.white);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            keyLocker.lockKey(Key.ESC);
            playLevelScreen.endSwapMoves();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        placeholderMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
}