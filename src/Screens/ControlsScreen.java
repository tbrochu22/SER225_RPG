package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import java.awt.*;

// This class is for the controls screen
public class ControlsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont controlsLabel;
    protected SpriteFont[] createdByLabel;
    protected SpriteFont returnInstructionsLabel;

    public ControlsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        controlsLabel = new SpriteFont("Controls", 15, 7, "Times New Roman", 30, Color.white);
        createdByLabel = new SpriteFont[] {
            new SpriteFont("WASD to Move", 130, 121, "Times New Roman", 20, Color.white),
            new SpriteFont("SHIFT to Sprint", 130, 151, "Times New Roman", 20, Color.white),
            new SpriteFont("E to Show Menu", 130, 181, "Times New Roman", 20, Color.white),
            new SpriteFont("SPACE to Enter/Exit", 130, 211, "Times New Roman", 20, Color.white),
        };
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "Times New Roman", 30, Color.white);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        controlsLabel.draw(graphicsHandler);
        for (SpriteFont label : createdByLabel) {
            label.draw(graphicsHandler);
        }
        returnInstructionsLabel.draw(graphicsHandler);
    }
}
