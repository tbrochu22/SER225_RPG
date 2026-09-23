package Game;

import Engine.GameWindow;
import Engine.ScreenManager;
import Music.MusicPlayer;

/*
 * The game starts here
 * This class starts the GameWindow, sets the first screen,
 * and starts the background music.
 */

public class Game {

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        GameWindow gameWindow = new GameWindow();
        ScreenManager screenManager = gameWindow.getScreenManager();
        screenManager.setCurrentScreen(new ScreenCoordinator());
        MusicPlayer music = new MusicPlayer();
        String musicPath = "src/music/initial_d___deja_vu.wav";
        // Start music
        music.playMusic(musicPath);
        gameWindow.startGame();
    }
}