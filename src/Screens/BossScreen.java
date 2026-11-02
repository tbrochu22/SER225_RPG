package Screens;

import Battle.Move;
import Engine.ImageLoader;
import Engine.ScreenManager;

public class BossScreen extends EncounterScreen{
    protected String bossName;
    protected int bossHp;
    protected int bossAttack;
    protected int bossExpReward;

    public BossScreen(PlayLevelScreen playLevelScreen, String bossName, String spriteFile, Move[] bossMoves, int hp, int attack, int expReward){
        super(playLevelScreen, bossMoves);
        this.bossName = bossName;
        this.bossHp = hp;
        this.bossAttack = attack;
        this.bossExpReward = expReward;
        enemySprite = ImageLoader.load(spriteFile);
    }
    public void startFight(){
        initialize();
        enemyLabel.setText(bossName);
        enemyLabel.setX(ScreenManager.getScreenWidth() - 160);
        enemyMaxHp = bossHp;
        enemyHp = bossHp;
        enemyAttack = bossAttack;
        expReward = bossExpReward;
    }
}
