package Level;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import SpriteFont.SpriteFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// Represents the game's textbox
// Will display the text it is given to its textQueue
// Each String in the textQueue will be displayed in the textbox, and hitting the interact key will cycle between additional Strings in the queue
// Use the newline character in a String in the textQueue to break the text up into a second line if needed
// Also supports adding options for a player to select from
public class Textbox {
    // whether textbox is shown or not
    protected boolean isActive;

    // textbox constants
    protected final int x = 22;
    protected final int bottomY = 460;
    protected final int topY = 22;
    protected final int rightSideX = 800 - 260;
    protected final int fontX = 35;
    protected final int fontBottomY = 472;
    protected final int fontTopY = 34;
    protected int width = 750;
    protected int height = 100;

    // options textbox constants
    protected int optionX = 680;
    protected final int optionBottomY = 350;
    protected final int optionTopY = 130;
    protected int optionWidth = 92;
    protected int optionHeight = 100;
    protected int fontOptionX = 706;
    protected final int fontOptionBottomYStart = 365;
    protected final int fontOptionTopYStart = 145;
    protected final int fontOptionSpacing = 35;
    protected int optionPointerX = 690;
    protected final int optionPointerYBottomStart = 378;
    protected final int optionPointerYTopStart = 158;

    // core vars that make textbox work
    private Queue<TextboxItem> textQueue;
    private TextboxItem currentTextItem;
    protected int selectedOptionIndex = 0;
    private SpriteFont text = null;
    private ArrayList<SpriteFont> options = null;
    private KeyLocker keyLocker = new KeyLocker();
    private Key interactKey = Key.SPACE;

    private Map map;
    private boolean forceLeftSide = false;

    public Textbox(Map map) {
        this.map = map;
        this.textQueue = new LinkedList<>();
    }

    public void addText(String text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        textQueue.add(new TextboxItem(text));
    }

    public void addText(String[] text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        for (String textItem : text) {
            textQueue.add(new TextboxItem(textItem));
        }
    }

    public void addText(TextboxItem text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        textQueue.add(text);
    }

    public void addText(TextboxItem[] text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        for (TextboxItem textItem : text) {
            textQueue.add(textItem);
        }
    }

    // returns whether the textQueue is out of items to display or not
    // useful for scripts to know when to complete
    public boolean isTextQueueEmpty() {
        return textQueue.isEmpty();
    }

    public void clear() {
        textQueue.clear();
        currentTextItem = null;
        text = null;
        options = null;
        selectedOptionIndex = 0;
    }

    public void update() {
        // if textQueue has more text to display and the interact key button was pressed previously, display new text
        if (!textQueue.isEmpty() && keyLocker.isKeyLocked(interactKey)) {
            currentTextItem = textQueue.peek();
            options = null;
            updateBoxSize();

            // if camera is at bottom of screen, text is drawn at top of screen instead of the bottom like usual
            // to prevent it from covering the player
            int fontY = !map.getCamera().isAtBottomOfMap() ? fontBottomY : fontTopY;
  
            // create text spritefont that will be drawn in textbox
            text = new SpriteFont(currentTextItem.getText(), fontX, fontY, "Arial", 30, Color.black);

            // if there are options associated with this text item, prepare option spritefont text to be drawn in options textbox
            if (currentTextItem.getOptions() != null) {
                // if camera is at bottom of screen, text is drawn at top of screen instead of the bottom like usual
                // to prevent it from covering the player
                int fontOptionY = !map.getCamera().isAtBottomOfMap() ? fontOptionBottomYStart : fontOptionTopYStart;

                options = new ArrayList<>();
                // for each option, crate option text spritefont that will be drawn in options textbox
                for (int i = 0; i < currentTextItem.getOptions().size(); i++) {
                    options.add(new SpriteFont(currentTextItem.options.get(i), fontOptionX, fontOptionY + (i *  fontOptionSpacing), "Arial", 30, Color.black));
                }
                selectedOptionIndex = 0;
            }

        }
        // if interact key is pressed, remove the current text from the queue to prepare for the next text item to be displayed
        if (Keyboard.isKeyDown(interactKey) && !keyLocker.isKeyLocked(interactKey)) {
            keyLocker.lockKey(interactKey);
            textQueue.poll();

            // if an option was selected, set output manager flag to the index of the selected option
            // a script can then look at output manager later to see which option was selected and do with that information what it wants
            if (options != null && map.getActiveScript() != null) {
                map.getActiveScript().getScriptActionOutputManager().addFlag("TEXTBOX_OPTION_SELECTION", selectedOptionIndex);
            }
        }
        else if (Keyboard.isKeyUp(interactKey)) {
            keyLocker.unlockKey(interactKey);
        }

        if (options != null) {
            if (Keyboard.isKeyDown(Key.S) && !keyLocker.isKeyLocked(Key.S)) {
                keyLocker.lockKey(Key.S);
                if (selectedOptionIndex < options.size() - 1) {
                    selectedOptionIndex++;
                }
            }
            if (Keyboard.isKeyDown(Key.W) && !keyLocker.isKeyLocked(Key.W)) {
                keyLocker.lockKey(Key.W);
                if (selectedOptionIndex > 0) {
                    selectedOptionIndex--;
                }
            }
            if (Keyboard.isKeyUp(Key.S)) {
                keyLocker.unlockKey(Key.S);
            }
            if (Keyboard.isKeyUp(Key.W)) {
                keyLocker.unlockKey(Key.W);
            }
        }
    }

    private void updateBoxSize() {
        int screenWidth = 800;
        int screenHeight = 600;

        if (currentTextItem == null) {
            width = 750;
            height = 100;
            optionWidth = 92;
            optionHeight = 100;
            optionX = 680;
            fontOptionX = 706;
            optionPointerX = 690;
            return;
        }

        String textToMeasure = currentTextItem.getText();
        String[] lines = textToMeasure.split("\\n");
        int longestLineLength = 0;
        for (String line : lines) {
            longestLineLength = Math.max(longestLineLength, line.length());
        }

        int estimatedWidth = Math.max(200, longestLineLength * 13 + 60);
        int estimatedHeight = Math.max(100, lines.length * 30 + 40);

        optionWidth = 92;
        optionHeight = 100;
        fontOptionX = 706;
        optionPointerX = 690;
        optionX = 680;

        if (currentTextItem.getOptions() != null && !currentTextItem.getOptions().isEmpty()) {
            int longestOptionLength = 0;
            for (String optionText : currentTextItem.getOptions()) {
                longestOptionLength = Math.max(longestOptionLength, optionText.length());
            }

            optionWidth = Math.max(92, Math.min(260, longestOptionLength * 16 + 50));
            optionHeight = Math.max(100, currentTextItem.getOptions().size() * fontOptionSpacing + 40);

            int idealWidth = Math.max(estimatedWidth, optionWidth + 70);
            width = Math.min(Math.max(idealWidth, 200), screenWidth - 80);

            optionX = rightSideX;
            fontOptionX = optionX + 26;
            optionPointerX = optionX + 10;
        }
        else {
            width = Math.min(Math.max(estimatedWidth, 200), screenWidth - 80);
        }

        height = Math.min(Math.max(estimatedHeight, 100), screenHeight - 180);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // draw textbox
        // if camera is at bottom of screen, textbox is drawn at top of screen instead of the bottom like usual
        // to prevent it from covering the player
        int y = !map.getCamera().isAtBottomOfMap() ? bottomY : topY;
        boolean useRightSide = !forceLeftSide && options != null;
        int boxX = useRightSide ? rightSideX : x;
        graphicsHandler.drawFilledRectangleWithBorder(boxX, y, width, height, Color.white, Color.black, 2);

        if (text != null) {
            // draw text in textbox
            int textX = useRightSide ? boxX + 15 : x + 15;
            text.drawWithParsedNewLines(graphicsHandler, textX - x + 10);
            
            if (options != null) {
                // draw options textbox
                // if camera is at bottom of screen, textbox is drawn at top of screen instead of the bottom like usual
                // to prevent it from covering the player
                int optionY = !map.getCamera().isAtBottomOfMap() ? optionBottomY : optionTopY;
                graphicsHandler.drawFilledRectangleWithBorder(optionX, optionY, optionWidth, optionHeight, Color.white, Color.black, 2);

                // draw each option text
                for (SpriteFont option : options) {
                    option.draw(graphicsHandler);
                }

                // the start y location of the option pointer depends on whether the options textbox is on top or bottom of screen
                int optionPointerYStart = !map.getCamera().isAtBottomOfMap() ? optionPointerYBottomStart : optionPointerYTopStart;
                // draw option selection indicator (small black rectangle)
                graphicsHandler.drawFilledRectangle(optionPointerX, optionPointerYStart + (selectedOptionIndex * fontOptionSpacing), 10, 10, Color.black);
            }
        }
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setInteractKey(Key interactKey) {
        this.interactKey = interactKey;
    }

    public void setForceLeftSide(boolean forceLeftSide) {
        this.forceLeftSide = forceLeftSide;
    }

}
