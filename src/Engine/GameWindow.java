package Engine;

import javax.swing.*;
import java.awt.*;

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

        gameWindow.setUndecorated(true);
        gamePanel.setFocusable(true);
        gameWindow.setContentPane(gamePanel);
        gameWindow.setResizable(false);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsConfiguration screen = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
        gameWindow.setBounds(screen.getBounds());
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