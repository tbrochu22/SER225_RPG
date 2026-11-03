package Screens;

import Battle.Bite;
import Battle.VampBite;
import Battle.Move;
import Battle.Roar;
import Battle.Scratch;
import Battle.Hawkeye;
import Battle.Brace;
import Battle.Growl;
import Battle.Haymaker;
import Battle.Slimeball;
import Battle.Honeycomb;
import Battle.Taunt;
import Battle.SacredFlame;
import Battle.electricBite;
import Engine.GraphicsHandler;
import Engine.Screen;
import EnhancedMapTiles.Rflower;
import Game.GameState;
import GameObject.Rectangle;
import Game.ScreenCoordinator;
import Level.*;
import Maps.RockyPathMap;
import Maps.PyramidMap;
import Maps.SecondMap;
import Maps.ThirdMap;
import Music.MusicPlayer;
import NPCs.RoamingEnemy;
import Players.Knight;
import Utils.Direction;
import Utils.Point;
import java.util.ArrayList;
import java.util.Random;
import NPCs.RoamingEnemy;
import Battle.HighJumpKick;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected SwapMovesScreen swapMovesScreen;
    protected FlagManager flagManager;
    protected EncounterScreen encounterScreen;
    protected String[] monsterNameStrings;
    protected java.util.Random random;
    protected Utils.Point lastTile;
    protected ArrayList<Rflower> flowers;
    protected BossScreen sparkWolfScreen, clawBearScreen, shadowBatScreen, slimeBossScreen,slimeBossScreen2;
    protected BossScreen[] randomBosses;
    protected BossScreen currentFight;
    protected RoamingEnemy currentRoamingEnemy;
    protected int enemyCooldown = 0;
    protected int roamingEnemiesDefeated = 0;
    public static final int ROAMING_KILLS_NEEDED = 3;

    // each map is only built once and then reused, so going back to a map keeps its state
    // (flowers already touched, items already picked up, boulders already cleared, etc.)
    protected RockyPathMap rockyPathMap;
    protected SecondMap secondMap;
    protected PyramidMap pyramidMap;
    protected ThirdMap thirdMap;
    // which boss the current/last boss fight is against (1 = slime on the rocky path, 2 = octopus in the desert)
    protected int currentBoss;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        // setup state
        flagManager = new FlagManager();
        flagManager.addFlag("hasLostBall", false);
        flagManager.addFlag("hasTalkedToWalrus", false);
        flagManager.addFlag("hasTalkedToDinosaur", false);
        flagManager.addFlag("hasFoundBall", false);
        flagManager.addFlag("hasBeatenBoss1", false);
        flagManager.addFlag("hasBeatenBoss2", false);
        flagManager.addFlag("archerQuestStarted", false);
        flagManager.addFlag("hasGift", false);
        flagManager.addFlag("archerQuestComplete", false);
        flagManager.addFlag("move1Unlocked", false);
        flagManager.addFlag("move2Unlocked", false);
        flagManager.addFlag("move3Unlocked", false);
        flagManager.addFlag("move4Unlocked", false);
        flagManager.addFlag("move5Unlocked", false);
        flagManager.addFlag("move6Unlocked", false);
        flagManager.addFlag("killQuestStarted", false);
        flagManager.addFlag("killQuestReady", false);
        flagManager.addFlag("killQuestComplete", false);
        flagManager.addFlag("move7Unlocked", false);
        roamingEnemiesDefeated = 0;

        // define/setup map
        rockyPathMap = new RockyPathMap();
        secondMap = null;
        pyramidMap = null;
        thirdMap = null;
        map = rockyPathMap;
        map.setFlagManager(flagManager);
        flowers = new ArrayList<>();
        for(EnhancedMapTile tile: map.getEnhancedMapTiles()){
            if(tile instanceof Rflower){
                flowers.add((Rflower) tile);
            }
        }

        // setup player
        player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);

        map.setPlayer(player);

        // let pieces of map know which button to listen for as the "interact" button
        map.getTextbox().setInteractKey(player.getInteractKey());

        // add this screen as a "game listener" so other areas of the game that don't normally have direct access to it (such as scripts) can "signal" to have it do something
        // this is used in the "onWin" method -- a script signals to this class that the game has been won by calling its "onWin" method
        map.addListener(this);

        // preloads all scripts ahead of time rather than loading them dynamically
        // both are supported, however preloading is recommended
        map.preloadScripts();

        winScreen = new WinScreen(this);
        encounterScreen = new EncounterScreen(this, new Move[] {
                new Roar(), new Bite(), new electricBite(), new Scratch()
        });

        sparkWolfScreen = new BossScreen(this, "Spark Wolf", "SparkWolf.png", new Move[] {new electricBite(), new Roar(), new Growl(), new Scratch()}, 90, 9, 30);
        clawBearScreen = new BossScreen(this, "Claw Bear", "ClawBear.png", new Move[] {new Honeycomb(), new Roar(), new Growl(), new Scratch()}, 120, 8, 35);
        shadowBatScreen = new BossScreen(this, "ShadowBat", "ShadowBat.png", new Move[] {new VampBite(), new Roar(), new Bite(), new Scratch()}, 80, 10, 30);
        slimeBossScreen = new BossScreen(this, "Slimeboss", "Boss1.png", new Move[] {new Slimeball(), new Growl(), new VampBite(), new Scratch()}, 150, 10, 60);
        slimeBossScreen2 = new BossScreen(this, "Raging Slime", "Boss2.png", new Move[] {new Haymaker(), new Roar(), new VampBite(), new Slimeball()}, 220, 12, 100);       
        randomBosses = new BossScreen[] { sparkWolfScreen, clawBearScreen, shadowBatScreen };
        lastTile = map.getTileIndexByPosition(player.getX(), player.getY());
        random = new Random();
        monsterNameStrings = new String[] {"Monster x", "Monster y", "Monster z"};
        swapMovesScreen = new SwapMovesScreen(this);
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                if (playLevelScreenState == PlayLevelScreenState.RUNNING) {
                    map.update(player);
                    checkForRoamingEnemyContact();
                    if(playLevelScreenState == playLevelScreenState.RUNNING){
                    encounterChance();
                    checkForMapExit();
                    }
                }
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
            case ENCOUNTER:
                currentFight.update();
                break;
            // if a fight has been triggered, bring up the cutaway screen
            case CUTAWAY:
                //cutawayScreen.update();
                break;
            case SWAP_MOVES:
                swapMovesScreen.update();
                break;
        }
    }

    private void checkForRoamingEnemyContact() {
    // don't start a fight in the middle of a conversation
    if (enemyCooldown > 0) {
        enemyCooldown--;
        return;
    }
    if (map.getTextbox().isActive()) {
        return;
    }
    for (NPC npc : map.getActiveNPCs()) {
        if (npc instanceof RoamingEnemy && npc.touching(player)) {
            currentRoamingEnemy = (RoamingEnemy) npc;
            currentBoss = 0;   // see the note below
            startBossFight(getBattleScreenFor(currentRoamingEnemy.getEnemyName()));
            MusicPlayer.playMusic("src/music/battle-music.wav");
            return;
        }
    }
    }

    private BossScreen getBattleScreenFor(String enemyName) {
    switch (enemyName) {
        case "Spark Wolf": return sparkWolfScreen;
        case "Claw Bear":  return clawBearScreen;
        case "Shadow Bat": return shadowBatScreen;
        default:           return sparkWolfScreen;
    }
    }

    @Override
    public void onWin() {
        // when this method is called within the game, it signals the game has been "won"
        playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
    }

    @Override
    public void onFight() {
        // when this method is called within the game, it signals that a boss fight has been triggered
        MusicPlayer.playMusic("src/music/battle-music.wav");
        // reset HP/menus so a rematch (after losing) starts fresh instead of reusing the last fight's state
        //cutawayScreen.initialize();
        //playLevelScreenState = PlayLevelScreenState.CUTAWAY;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                break;
            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;
            case ENCOUNTER:
                currentFight.draw(graphicsHandler);
            break;
            case CUTAWAY:
                //cutawayScreen.draw(graphicsHandler);
                break;
            case SWAP_MOVES:
                swapMovesScreen.draw(graphicsHandler);
                break;
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // called by CutawayScreen when the Boss1 fight is over
    // winning clears the boulders at the end of the rocky path; losing sends the player back to the map start like a normal encounter
    public void returnFromBossFight(boolean playerLost) {
        if (!playerLost) {
            if (currentBoss == 1) {
                onBoss1Defeated();
            } else if (currentBoss == 2) {
                onBoss2Defeated();
            }
        }
        returnFromEncounter(playerLost);
    }

    public void endCutaway() {
        // returns control back to the map/player once the cutaway screen is dismissed
        MusicPlayer.playMusic("src/music/Inductance.wav");
        playLevelScreenState = PlayLevelScreenState.RUNNING;
    }

    // ---------------- map transitions ----------------

    // rocky path exit: bottom of the path, these tiles start as boulders in Rocky_path_map.txt
    private static final int ROCKY_EXIT_ROW = 39;
    private static final int ROCKY_EXIT_START_X = 27;
    private static final int ROCKY_EXIT_END_X = 29;
    // where the player lands on the rocky path when coming back from the desert (just above the exit)
    private static final int ROCKY_RETURN_X = 28;
    private static final int ROCKY_RETURN_Y = 37;

    // desert exit: the opening in the top wall of second_map.txt
    private static final int DESERT_EXIT_ROW = 0;
    private static final int DESERT_EXIT_START_X = 19;
    private static final int DESERT_EXIT_END_X = 20;
    // where the player lands in the desert when coming from the rocky path (just below the opening)
    private static final int DESERT_ARRIVAL_X = 19;
    private static final int DESERT_ARRIVAL_Y = 1;

    // desert exit past Boss2: these tiles start as boulders in the bottom wall of second_map.txt
    private static final int DESERT_BOSS_EXIT_ROW = 29;
    private static final int DESERT_BOSS_EXIT_START_X = 4;
    private static final int DESERT_BOSS_EXIT_END_X = 5;

    // where the player lands in the desert when coming back from the volcano map (just above the boss exit)
    private static final int DESERT_FROM_VOLCANO_X = 4;
    private static final int DESERT_FROM_VOLCANO_Y = 26;

    // volcano map exit: the opening in the top-right wall of third_map.txt, and where the player lands coming in (just below it)
    private static final int VOLCANO_EXIT_ROW = 0;
    private static final int VOLCANO_EXIT_START_X = 35;
    private static final int VOLCANO_EXIT_END_X = 36;
    private static final int VOLCANO_ARRIVAL_X = 35;
    private static final int VOLCANO_ARRIVAL_Y = 1;

    // the pyramid's doorway in the desert, and where the player lands when walking back out (just below it)
    private static final int PYRAMID_DOOR_X = 31;
    private static final int PYRAMID_DOOR_Y = 17;
    private static final int DESERT_FROM_PYRAMID_X = 31;
    private static final int DESERT_FROM_PYRAMID_Y = 19;

    // the doorway inside the pyramid (bottom wall of pyramid_map.txt), and where the player lands when walking in
    private static final int PYRAMID_INSIDE_DOOR_X = 9;
    private static final int PYRAMID_INSIDE_DOOR_Y = 13;
    private static final int PYRAMID_ARRIVAL_X = 9;
    private static final int PYRAMID_ARRIVAL_Y = 11;

    // marks Boss1 as beaten and clears the boulders blocking the end of the path
    public void onBoss1Defeated() {
        if (!flagManager.isFlagSet("hasBeatenBoss1")) {
            flagManager.setFlag("hasBeatenBoss1");
            flagManager.setFlag("move5Unlocked");
            map.getTextbox().addText("As the slime disolves..."); 
            map.getTextbox().addText("The boulders blocking the path crumble away.");
            map.getTextbox().addText("You can now continue down the path.");
            map.getTextbox().addText("You also decide to keep the slime's remains.");
            map.getTextbox().addText("And you form them into a ball.");
            map.getTextbox().addText("You can now use " + new Slimeball().getName() + " as a new move in battle!");
            map.getTextbox().addText("Use 'E' to open the menu and equip it!");
            map.getTextbox().setIsActive(true);
            player.lock();
        }
        openRockyPathExit();
    }

    public void onBoss2Defeated() {
        if (!flagManager.isFlagSet("hasBeatenBoss2")) {
            flagManager.setFlag("hasBeatenBoss2");
            flagManager.setFlag("move6Unlocked");
            map.getTextbox().addText("The slime runs off...");
            map.getTextbox().addText("But dropped something as it fled.");
            map.getTextbox().addText("It was a huge sword!");
            map.getTextbox().addText("You feel that this will be very useful!");
            map.getTextbox().addText("You can now use " + new Haymaker().getName()
                    + " as a new move in battle!");
            map.getTextbox().addText("Use 'E' to open the menu and equip it!");
            map.getTextbox().setIsActive(true);
            player.lock();
            openDesertBossExit();
        }
    }

    // swaps the exit boulders on the rocky path for dirt path tiles (safe to call more than once)
    private void openRockyPathExit() {
        for (int x = ROCKY_EXIT_START_X; x <= ROCKY_EXIT_END_X; x++) {
            Point location = rockyPathMap.getMapTile(x, ROCKY_EXIT_ROW).getLocation();
            // tile index 1 in RockyTileset is the dirt path (passable)
            MapTile pathTile = rockyPathMap.getTileset().getTile(1).build(location.x, location.y);
            rockyPathMap.setMapTile(x, ROCKY_EXIT_ROW, pathTile);
        }
    }

    // marks Boss2 as beaten and clears the boulders blocking the end of the desert path
    /*public void onBoss2Defeated() {
        if (!flagManager.isFlagSet("hasBeatenBoss2")) {
            flagManager.setFlag("hasBeatenBoss2");
        }
        openDesertBossExit();
    }
    */
    // swaps the boulders past Boss2 for dirt path tiles (safe to call more than once)
    private void openDesertBossExit() {
        if (secondMap == null) {
            return;
        }
        for (int x = DESERT_BOSS_EXIT_START_X; x <= DESERT_BOSS_EXIT_END_X; x++) {
            Point location = secondMap.getMapTile(x, DESERT_BOSS_EXIT_ROW).getLocation();
            // tile index 1 in DesertTileset is the dirt path (passable)
            MapTile pathTile = secondMap.getTileset().getTile(1).build(location.x, location.y);
            secondMap.setMapTile(x, DESERT_BOSS_EXIT_ROW, pathTile);
        }
    }

    // true when the player is pressed up against a one-tile doorway
    // (the knight's body is exactly one tile wide, so instead of having to squeeze fully into the doorway,
    // touching it while lined up with it is enough)
    // doorIsAbove = the doorway is above the player (walking up into it), otherwise it's below (walking down into it)
    private boolean isTouchingDoorway(int doorTileX, int doorTileY, boolean doorIsAbove) {
        int tileSize = map.getTileset().getScaledSpriteWidth();
        float doorLeft = doorTileX * tileSize;
        float doorTop = doorTileY * tileSize;
        Rectangle body = player.getBounds();
        float centerX = (body.getX1() + body.getX2()) / 2f;
        boolean linedUp = centerX >= doorLeft + 8 && centerX <= doorLeft + tileSize - 8;
        if (!linedUp) {
            return false;
        }
        if (doorIsAbove) {
            return body.getY1() <= doorTop + tileSize + 2;
        } else {
            return body.getY2() >= doorTop - 2;
        }
    }

    // checks every frame whether the player is standing in a map's exit, and if so moves them to the connected map
    private void checkForMapExit() {
        Point currentTile = map.getTileIndexByPosition(player.getX(), player.getY());

        if (map == rockyPathMap) {
            // exit only works once Boss1 is beaten (before that it's blocked by boulders anyway)
            // row 38 also counts so the player doesn't have to squeeze fully into the bottom row
            if (flagManager.isFlagSet("hasBeatenBoss1")
                    && currentTile.y >= ROCKY_EXIT_ROW - 1
                    && currentTile.x >= ROCKY_EXIT_START_X && currentTile.x <= ROCKY_EXIT_END_X) {
                if (secondMap == null) {
                    secondMap = new SecondMap();
                }
                loadMap(secondMap, DESERT_ARRIVAL_X, DESERT_ARRIVAL_Y, Direction.DOWN);
            }
        } else if (map == secondMap) {
            // walking back up through the opening in the top wall returns to the rocky path
            if (currentTile.y <= DESERT_EXIT_ROW
                    && currentTile.x >= DESERT_EXIT_START_X && currentTile.x <= DESERT_EXIT_END_X) {
                loadMap(rockyPathMap, ROCKY_RETURN_X, ROCKY_RETURN_Y, Direction.UP);
            }
            // walking up into the pyramid's doorway goes inside
            else if (isTouchingDoorway(PYRAMID_DOOR_X, PYRAMID_DOOR_Y, true)) {
                if (pyramidMap == null) {
                    pyramidMap = new PyramidMap();
                }
                loadMap(pyramidMap, PYRAMID_ARRIVAL_X, PYRAMID_ARRIVAL_Y, Direction.UP);
            }
            // once Boss2 is beaten, walking down through the opened exit below it goes to the volcano map
            // (row 28 also counts so the player doesn't have to squeeze fully into the bottom row)
            else if (flagManager.isFlagSet("hasBeatenBoss2")
                    && currentTile.y >= DESERT_BOSS_EXIT_ROW - 1
                    && currentTile.x >= DESERT_BOSS_EXIT_START_X && currentTile.x <= DESERT_BOSS_EXIT_END_X) {
                if (thirdMap == null) {
                    thirdMap = new ThirdMap();
                }
                loadMap(thirdMap, VOLCANO_ARRIVAL_X, VOLCANO_ARRIVAL_Y, Direction.DOWN);
            }
        } else if (map == thirdMap) {
            // walking back up through the opening in the top-right wall returns to the desert, just above the boss exit
            if (currentTile.y <= VOLCANO_EXIT_ROW
                    && currentTile.x >= VOLCANO_EXIT_START_X && currentTile.x <= VOLCANO_EXIT_END_X) {
                loadMap(secondMap, DESERT_FROM_VOLCANO_X, DESERT_FROM_VOLCANO_Y, Direction.UP);
            }
        } else if (map == pyramidMap) {
            // walking down into the doorway in the bottom wall goes back out to the desert, just below the pyramid
            if (isTouchingDoorway(PYRAMID_INSIDE_DOOR_X, PYRAMID_INSIDE_DOOR_Y, false)) {
                loadMap(secondMap, DESERT_FROM_PYRAMID_X, DESERT_FROM_PYRAMID_Y, Direction.DOWN);
            }
        }
    }

    // swaps in another map and places the player on the given tile, keeping flags and player state
    // the arrival tile must be OUTSIDE that map's exit area, otherwise the player would bounce straight back
    public void loadMap(Map newMap, int tileX, int tileY, Direction facing) {
        boolean firstVisit = newMap.getPlayer() == null;

        map = newMap;
        map.setFlagManager(flagManager);

        player.setMap(map);
        Point arrival = map.getMapTile(tileX, tileY).getLocation();
        player.setLocation(arrival.x, arrival.y);
        player.setFacingDirection(facing);
        map.setPlayer(player);

        // only hook up listeners/scripts the first time a map is used, otherwise they'd be added twice
        // and things like onFight() would fire twice
        if (firstVisit) {
            map.getTextbox().setInteractKey(player.getInteractKey());
            map.addListener(this);
            map.preloadScripts();
        }

        if (map == rockyPathMap && flagManager.isFlagSet("hasBeatenBoss1")) {
            openRockyPathExit();
        }
        if (map == secondMap && flagManager.isFlagSet("hasBeatenBoss2")) {
            openDesertBossExit();
        }

        flowers.clear();
        for (EnhancedMapTile tile : map.getEnhancedMapTiles()) {
            if (tile instanceof Rflower) {
                flowers.add((Rflower) tile);
            }
        }
        lastTile = map.getTileIndexByPosition(player.getX(), player.getY());
    }

    @Override
    public void onSwapMoves() {
        swapMovesScreen.initialize();
        playLevelScreenState = PlayLevelScreenState.SWAP_MOVES;
    }

    public void endSwapMoves() {
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.openMenu();
    }

    public Move[] getEquippedMoves() {
        return player.getEquippedMoves();
    }

    public Player getPlayer(){
        return player;
    }
    public Move[] getAvailableMoves() {
        return player.getAvailableMoves();
    }

    public boolean isMoveUnlocked(Move move) {
        if (move instanceof Hawkeye) {
            return flagManager.isFlagSet("move1Unlocked");
        }
        if (move instanceof Brace) {
            return flagManager.isFlagSet("move2Unlocked");
        }
        if (move instanceof Taunt) {
            return flagManager.isFlagSet("move3Unlocked");
        }
        if (move instanceof SacredFlame) {
            return flagManager.isFlagSet("move4Unlocked");
        }
        if (move instanceof Slimeball) {
            return flagManager.isFlagSet("move5Unlocked");
        }
        if (move instanceof Haymaker) {
            return flagManager.isFlagSet("move6Unlocked");
        }
        if (move instanceof HighJumpKick) {
            return flagManager.isFlagSet("move7Unlocked");
        }
        
        return true;
    }

    public void equipMove(int moveIndex, Move move) {
        if (!isMoveUnlocked(move)) {
            throw new IllegalArgumentException("Move must be unlocked before it can be equipped.");
        }
        player.equipMove(moveIndex, move);
    }

    public void swapEquippedMoves(int firstMoveIndex, int secondMoveIndex) {
        player.swapEquippedMoves(firstMoveIndex, secondMoveIndex);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, ENCOUNTER, CUTAWAY, SWAP_MOVES
    }

    public void returnFromEncounter(boolean playerLost){
        if (playerLost) {
            Point startPosition = map.getPlayerStartPosition();
            player.setLocation(startPosition.x, startPosition.y);
            player.setFacingDirection(Direction.DOWN);
            lastTile = map.getTileIndexByPosition(player.getX(), player.getY());
        }
        if (currentRoamingEnemy != null) {
            if (!playerLost) {
            currentRoamingEnemy.setMapEntityStatus(MapEntityStatus.REMOVED);
            onRoamingEnemyDefeated();
            }
            currentRoamingEnemy = null;
        }
enemyCooldown = 60;
        MusicPlayer.playMusic("src/music/Inductance.wav");
        playLevelScreenState = PlayLevelScreenState.RUNNING;

    }
        // counts a roaming enemy kill toward the Mage's quest and tells the player their progress
    private void onRoamingEnemyDefeated() {
        roamingEnemiesDefeated++;
        if (roamingEnemiesDefeated == ROAMING_KILLS_NEEDED) {
            flagManager.setFlag("killQuestReady");
        }
        if (!flagManager.isFlagSet("killQuestStarted") || flagManager.isFlagSet("killQuestComplete")) {
            return;   // only show progress while the quest is active
        }
        if (roamingEnemiesDefeated < ROAMING_KILLS_NEEDED) {
            map.getTextbox().addText("Roaming enemies defeated: " + roamingEnemiesDefeated + "/" + ROAMING_KILLS_NEEDED);
        } else {
            map.getTextbox().addText("Roaming enemies defeated: " + ROAMING_KILLS_NEEDED + "/" + ROAMING_KILLS_NEEDED + "!");
            map.getTextbox().addText("Go back and tell the Mage!");
        }
        map.getTextbox().setIsActive(true);
        player.lock();
    }
    public void startBossFight(BossScreen boss) {
    currentFight = boss;
    currentFight.startFight();
    playLevelScreenState = PlayLevelScreenState.ENCOUNTER;
    }
    public void encounterChance(){
        //essentially this chunk checks if the spot the player is now is different from the last time lastTile was saved
        Point currentTile = map.getTileIndexByPosition(player.getX(), player.getY());
        for(Rflower flower: flowers){
            Point flowerTile = map.getTileIndexByPosition(flower.getX(), flower.getY());
            if(currentTile.x == flowerTile.x && currentTile.y == flowerTile.y){
                if (currentTile.x != lastTile.x || currentTile.y != lastTile.y) {
                    if(random.nextFloat() <= 1f){
                    encounterScreen.setMonsterName(monsterNameStrings[random.nextInt(monsterNameStrings.length)]);
                    MusicPlayer.playMusic("src/music/battle-music.wav");
                    playLevelScreenState = PlayLevelScreenState.ENCOUNTER;
                    }
                }
            }
        }
        lastTile = map.getTileIndexByPosition(player.getX(), player.getY());
        //System.out.println(currentTile + " " + flowerTile);
    }
}