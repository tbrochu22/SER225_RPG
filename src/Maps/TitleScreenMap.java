package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
import Utils.Colors;

// Background for the main menu and credits screen: a grassy meadow full of swaying flowers, with the knight standing in the middle of the screen
// (the flowers are CommonTileset's animated yellow and purple flower tiles, so they sway on the menu)
public class TitleScreenMap extends Map {

    private Sprite knight;

    public TitleScreenMap() {
        super("title_screen_map.txt", new CommonTileset());

        // first frame of the knight sprite sheet, drawn bigger than in-game (scale 5 = 120x120) and centred on the screen
        knight = new Sprite(ImageLoader.loadSubImage("KnightSheet.png", Colors.MAGENTA, 0, 0, 24, 24));
        knight.setScale(5);
        int knightSize = 24 * 5;
        int screenCenterX = 400;
        int screenCenterY = 302;
        knight.setLocation(screenCenterX - knightSize / 2, screenCenterY - knightSize / 2);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        knight.draw(graphicsHandler);
    }
}