package Engine;

import javax.swing.*;

/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
    private JFrame gameWindow;
    private GamePanel gamePanel;

    public GameWindow() {
        gameWindow = new JFrame("Game");
        gamePanel = new GamePanel();

        gamePanel.setFocusable(true);
        gameWindow.setContentPane(gamePanel);
        gameWindow.setResizable(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Maximize the game window
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setVisible(true);

        // Initialize the game
        gamePanel.setupGame();
        gamePanel.requestFocusInWindow();
    }

    public void startGame() {
        gamePanel.startGame();
    }

    public ScreenManager getScreenManager() {
        return gamePanel.getScreenManager();
    }
}