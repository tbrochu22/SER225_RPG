package Game;

import Engine.GameWindow;
import Engine.ScreenManager;
import javax.swing.SwingUtilities;

/*
 * The game starts here
 * This class starts the GameWindow, sets the first screen,
 * and starts the background music.
 */
// Added comment
public class Game {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Game();
        });
    }

    public Game() {
        GameWindow gameWindow = new GameWindow();
        ScreenManager screenManager = gameWindow.getScreenManager();
        screenManager.setCurrentScreen(new ScreenCoordinator());
        gameWindow.startGame();
    }
}