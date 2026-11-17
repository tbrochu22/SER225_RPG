package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont title;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont controls;
    protected SpriteFont exit;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        title = new SpriteFont("Rise of the Slimes", 100, 100, "Arial", 75, new Color(0, 255, 0));
        title.setOutlineColor(Color.black);
        title.setOutlineThickness(3);
        playGame = new SpriteFont("PLAY GAME", 200, 223, "Arial", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 200, 323, "Arial", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        controls = new SpriteFont("CONTROLS", 200, 423, "Arial", 30, new Color(49, 207, 240));
        controls.setOutlineColor(Color.black);
        controls.setOutlineThickness(3);
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        exit = new SpriteFont("EXIT GAME", 200, 523, "Arial", 30, new Color(49, 207, 240));
        exit.setOutlineColor(Color.black);
        exit.setOutlineThickness(3);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // W and S move the menu selection up and down.
        if (Keyboard.isKeyDown(Key.S) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.W) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 3) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 3;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(Color.YELLOW);
            credits.setColor(new Color(49, 207, 240));
            controls.setColor(new Color(49, 207, 240));
            exit.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 230;

        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(49, 207, 240));
            credits.setColor(Color.YELLOW);
            controls.setColor(new Color(49, 207, 240));
            exit.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 330;

        } else if (currentMenuItemHovered == 2) {
            playGame.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            controls.setColor(Color.YELLOW);
            exit.setColor(new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 430;
        } else if (currentMenuItemHovered == 3) {
            playGame.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            controls.setColor(new Color(49, 207, 240));
            exit.setColor(Color.YELLOW);
            pointerLocationX = 170;
            pointerLocationY = 530;
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.LEVEL);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
            else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CONTROLS);
            } else if (menuItemSelected == 3) {
                System.exit(0);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        title.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        controls.draw(graphicsHandler);
        exit.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }
}
