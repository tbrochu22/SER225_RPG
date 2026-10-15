package Screens;

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

        // define/setup map
        map = new RockyPathMap();
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

    public void endCutaway() {
        // the real boss fight isn't built yet, so for now leaving the cutaway screen counts as beating Boss1
        // TODO: once the fight exists, only call this when the player actually wins
        onBoss1Defeated();
        // returns control back to the map/player once the cutaway screen is dismissed
        playLevelScreenState = PlayLevelScreenState.RUNNING;
    }

    // exit at the bottom of the rocky path -- these tiles start as boulders in Rocky_path_map.txt
    private static final int EXIT_ROW = 39;
    private static final int EXIT_START_X = 27;
    private static final int EXIT_END_X = 29;

    // marks Boss1 as beaten and clears the boulders blocking the end of the path
    public void onBoss1Defeated() {
        if (flagManager.isFlagSet("hasBeatenBoss1")) {
            return;
        }
        flagManager.setFlag("hasBeatenBoss1");
        if (map instanceof RockyPathMap) {
            for (int x = EXIT_START_X; x <= EXIT_END_X; x++) {
                Point location = map.getMapTile(x, EXIT_ROW).getLocation();
                // tile index 1 in RockyTileset is the dirt path (passable)
                MapTile pathTile = map.getTileset().getTile(1).build(location.x, location.y);
                map.setMapTile(x, EXIT_ROW, pathTile);
            }
        }
    }

    // once the boulders are gone, walking to the end of the path transports the player to the next map
    private void checkForMapExit() {
        if (!(map instanceof RockyPathMap) || !flagManager.isFlagSet("hasBeatenBoss1")) {
            return;
        }
        Point currentTile = map.getTileIndexByPosition(player.getX(), player.getY());
        // row 38 also counts so the player doesn't have to squeeze fully into the bottom row
        if (currentTile.y >= EXIT_ROW - 1 && currentTile.x >= EXIT_START_X && currentTile.x <= EXIT_END_X) {
            loadMap(new SecondMap());
        }
    }

    // swaps in a new map and moves the player to its start position, keeping flags and player state
    public void loadMap(Map newMap) {
        map = newMap;
        map.setFlagManager(flagManager);

        player.setMap(map);
        player.setLocation(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setFacingDirection(Direction.DOWN);
        map.setPlayer(player);

        map.getTextbox().setInteractKey(player.getInteractKey());
        map.addListener(this);
        map.preloadScripts();

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

    public void returnFromEncounter(){
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