package Screens;

import Battle.Move;
import Engine.ImageLoader;
import Engine.ScreenManager;

public class BossScreen extends EncounterScreen{
    protected String bossName;
    public BossScreen(PlayLevelScreen playLevelScreen, String bossName, String spriteFile, Move[] bossMoves){
        super(playLevelScreen);
        this.bossName = bossName;
        enemySprite = ImageLoader.load(spriteFile);
        enemyMoves = bossMoves;
    }
    public void startFight(){
        initialize();
        enemyLabel.setText(bossName);
        enemyLabel.setX(ScreenManager.getScreenWidth() - 160);
    }
}
