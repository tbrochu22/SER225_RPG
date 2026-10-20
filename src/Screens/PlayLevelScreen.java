package Screens;

import Battle.Bite;
import Battle.Move;
import Battle.Roar;
import Battle.Scratch;
import Battle.electricBite;
import Engine.GraphicsHandler;
import Engine.Screen;
import EnhancedMapTiles.Rflower;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.RockyPathMap;
import Maps.SecondMap;
import Music.MusicPlayer;
import Players.Knight;
import Utils.Direction;
import Utils.Point;
import java.util.ArrayList;
import java.util.Random;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected CutawayScreen cutawayScreen;
    protected SwapMovesScreen swapMovesScreen;
    protected FlagManager flagManager;
    protected EncounterScreen encounterScreen;
    protected String[] monsterNameStrings;
    protected java.util.Random random;
    protected Utils.Point lastTile;
    protected ArrayList<Rflower> flowers;
    protected BossScreen sparkWolfScreen, clawBearScreen;

    // each map is only built once and then reused, so going back to a map keeps its state
    // (flowers already touched, items already picked up, boulders already cleared, etc.)
    protected RockyPathMap rockyPathMap;
    protected SecondMap secondMap;
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

        // define/setup map
        rockyPathMap = new RockyPathMap();
        secondMap = null;
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
        encounterScreen = new EncounterScreen(this);

        sparkWolfScreen = new BossScreen(this, "Spark Wolf", "SparkWolf.png", new Move[] {new electricBite(), new Roar(), new Bite(), new Scratch()});
        clawBearScreen = new BossScreen(this, "Claw Bear", "ClawBear.png", new Move[] {new Scratch(), new Roar(), new Bite(), new Scratch()});
        lastTile = map.getTileIndexByPosition(player.getX(), player.getY());
        random = new Random();
        monsterNameStrings = new String[] {"Monster x", "Monster y", "Monster z"};
        cutawayScreen = new CutawayScreen(this);
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
                    encounterChance();
                    checkForMapExit();
                }
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
            case ENCOUNTER:
                encounterScreen.update();
                break;
            // if a fight has been triggered, bring up the cutaway screen
            case CUTAWAY:
                cutawayScreen.update();
                break;
            case SWAP_MOVES:
                swapMovesScreen.update();
                break;
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
        cutawayScreen.initialize();
        // each map has one boss, so the current map tells us which boss was talked to
        currentBoss = (map == secondMap) ? 2 : 1;
        playLevelScreenState = PlayLevelScreenState.CUTAWAY;
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
                encounterScreen.draw(graphicsHandler);
            break;
            case CUTAWAY:
                cutawayScreen.draw(graphicsHandler);
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
                flagManager.setFlag("hasBeatenBoss2");
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
    private static final int DESERT_EXIT_START_X = 9;
    private static final int DESERT_EXIT_END_X = 10;
    // where the player lands in the desert when coming from the rocky path (just below the opening)
    private static final int DESERT_ARRIVAL_X = 9;
    private static final int DESERT_ARRIVAL_Y = 1;

    // marks Boss1 as beaten and clears the boulders blocking the end of the path
    public void onBoss1Defeated() {
        if (!flagManager.isFlagSet("hasBeatenBoss1")) {
            flagManager.setFlag("hasBeatenBoss1");
        }
        openRockyPathExit();
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
        playLevelScreenState = PlayLevelScreenState.SWAP_MOVES;
    }

    public void endSwapMoves() {
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.openMenu();
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
        MusicPlayer.playMusic("src/music/Inductance.wav");
        playLevelScreenState = PlayLevelScreenState.RUNNING;

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