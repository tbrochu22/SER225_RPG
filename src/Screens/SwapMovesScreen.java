package Screens;

import Battle.Move;
import Engine.*;

import java.awt.*;

public class SwapMovesScreen extends Screen {
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;
    private int selectedMoveIndex;
    private int firstMoveToSwap = -1;
    private int selectedAvailableMoveIndex;
    private boolean selectingReplacement;

    public SwapMovesScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        selectedMoveIndex = 0;
        firstMoveToSwap = -1;
        selectedAvailableMoveIndex = 0;
        selectingReplacement = false;
        keyLocker.lockKey(Key.W);
        keyLocker.lockKey(Key.A);
        keyLocker.lockKey(Key.S);
        keyLocker.lockKey(Key.D);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (selectingReplacement) {
            handleReplacementNavigationKey(Key.W);
            handleReplacementNavigationKey(Key.A);
            handleReplacementNavigationKey(Key.S);
            handleReplacementNavigationKey(Key.D);
        } else {
            handleNavigationKey(Key.W);
            handleNavigationKey(Key.A);
            handleNavigationKey(Key.S);
            handleNavigationKey(Key.D);
        }

        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
            keyLocker.lockKey(Key.ENTER);
            if (selectingReplacement) {
                Move[] availableMoves = playLevelScreen.getAvailableMoves();
                if (playLevelScreen.isMoveUnlocked(availableMoves[selectedAvailableMoveIndex])) {
                    playLevelScreen.equipMove(firstMoveToSwap, availableMoves[selectedAvailableMoveIndex]);
                    firstMoveToSwap = -1;
                    selectingReplacement = false;
                }
            } else {
                firstMoveToSwap = selectedMoveIndex;
                selectedAvailableMoveIndex = 0;
                selectingReplacement = true;
            }
        }

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            keyLocker.lockKey(Key.ESC);
            if (selectingReplacement) {
                selectingReplacement = false;
                firstMoveToSwap = -1;
            } else {
                playLevelScreen.endSwapMoves();
            }
        }
    }

    private void handleReplacementNavigationKey(Key key) {
        if (Keyboard.isKeyUp(key)) {
            keyLocker.unlockKey(key);
        }
        if (!Keyboard.isKeyDown(key) || keyLocker.isKeyLocked(key)) {
            return;
        }

        int availableMoveCount = playLevelScreen.getAvailableMoves().length;
        int rowCount = Math.min(5, availableMoveCount);
        int columnCount = (availableMoveCount + rowCount - 1) / rowCount;
        int column = selectedAvailableMoveIndex / rowCount;
        int row = selectedAvailableMoveIndex % rowCount;
        int selectedColumnSize = Math.min(rowCount, availableMoveCount - column * rowCount);
        int nextIndex = selectedAvailableMoveIndex;
        switch (key) {
            case W:
                if (row > 0) {
                    nextIndex--;
                }
                break;
            case A:
                if (column > 0) {
                    nextIndex -= rowCount;
                }
                break;
            case S:
                if (row + 1 < selectedColumnSize) {
                    nextIndex++;
                }
                break;
            case D:
                if (column + 1 < columnCount
                        && selectedAvailableMoveIndex + rowCount < availableMoveCount) {
                    nextIndex += rowCount;
                }
                break;
            default:
                return;
        }
        selectedAvailableMoveIndex = nextIndex;
        keyLocker.lockKey(key);
    }

    private void handleNavigationKey(Key key) {
        if (Keyboard.isKeyUp(key)) {
            keyLocker.unlockKey(key);
        }
        if (!Keyboard.isKeyDown(key) || keyLocker.isKeyLocked(key)) {
            return;
        }

        int column = selectedMoveIndex % 2;
        int row = selectedMoveIndex / 2;
        switch (key) {
            case W:
                row = Math.max(0, row - 1);
                break;
            case A:
                column = Math.max(0, column - 1);
                break;
            case S:
                row = Math.min(1, row + 1);
                break;
            case D:
                column = Math.min(1, column + 1);
                break;
            default:
                return;
        }
        selectedMoveIndex = row * 2 + column;
        keyLocker.lockKey(key);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        Graphics2D graphics = graphicsHandler.getGraphics();
        Font titleFont = new Font("Arial", Font.BOLD, 32);
        Font moveFont = new Font("Arial", Font.BOLD, 24);
        Font slotFont = new Font("Arial", Font.PLAIN, 14);
        Font instructionsFont = new Font("Arial", Font.PLAIN, 18);

        Move[] equippedMoves = playLevelScreen.getEquippedMoves();
        if (selectingReplacement) {
            drawAvailableMoves(graphicsHandler, graphics, titleFont, moveFont, instructionsFont,
                    equippedMoves);
            return;
        }

        String title = "Swap Equipped Moves";
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        graphicsHandler.drawString(title,
                (ScreenManager.getScreenWidth() - titleMetrics.stringWidth(title)) / 2,
                65, titleFont, Color.white);

        for (int index = 0; index < equippedMoves.length; index++) {
            int column = index % 2;
            int row = index / 2;
            int x = 100 + column * 340;
            int y = 130 + row * 125;
            Color borderColor = index == selectedMoveIndex ? Color.yellow : Color.white;
            if (index == firstMoveToSwap) {
                borderColor = Color.cyan;
            }
            graphicsHandler.drawFilledRectangleWithBorder(
                    x, y, 280, 80, new Color(40, 40, 65), borderColor, 3);

            String moveName = equippedMoves[index].getName();
            FontMetrics moveMetrics = graphics.getFontMetrics(moveFont);
            graphicsHandler.drawString("Move " + (index + 1),
                    x + 12, y + 24, slotFont, Color.lightGray);
            graphicsHandler.drawString(moveName,
                    x + (280 - moveMetrics.stringWidth(moveName)) / 2,
                    y + (80 + moveMetrics.getAscent() - moveMetrics.getDescent()) / 2,
                    moveFont, Color.white);
        }

        drawMoveDescription(graphicsHandler, graphics, equippedMoves[selectedMoveIndex]);

        String swapInstructions = "WASD: Select a slot    Enter: Choose a move    Escape: Return";
        FontMetrics instructionsMetrics = graphics.getFontMetrics(instructionsFont);
        graphicsHandler.drawString(swapInstructions,
                (ScreenManager.getScreenWidth() - instructionsMetrics.stringWidth(swapInstructions)) / 2,
                ScreenManager.getScreenHeight() - 24, instructionsFont, Color.white);
    }

    private void drawAvailableMoves(GraphicsHandler graphicsHandler, Graphics2D graphics, Font titleFont,
            Font moveFont, Font instructionsFont, Move[] equippedMoves) {
        Move[] availableMoves = playLevelScreen.getAvailableMoves();
        String title = "Choose a Replacement for " + equippedMoves[firstMoveToSwap].getName();
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        graphicsHandler.drawString(title,
                (ScreenManager.getScreenWidth() - titleMetrics.stringWidth(title)) / 2,
                65, titleFont, Color.white);

        int rowCount = Math.min(5, availableMoves.length);
        int columnCount = (availableMoves.length + rowCount - 1) / rowCount;
        int cellWidth = ScreenManager.getScreenWidth() / columnCount;
        for (int index = 0; index < availableMoves.length; index++) {
            int column = index / rowCount;
            int row = index % rowCount;
            int x = column * cellWidth + 20;
            int y = 100 + row * 75;
            int cardWidth = cellWidth - 40;
            boolean alreadyEquipped = false;
            for (Move equippedMove : equippedMoves) {
                if (availableMoves[index] == equippedMove) {
                    alreadyEquipped = true;
                    break;
                }
            }
            boolean unlocked = playLevelScreen.isMoveUnlocked(availableMoves[index]);
            Color fillColor = alreadyEquipped || !unlocked
                    ? new Color(50, 50, 50)
                    : new Color(40, 40, 65);
            Color borderColor = index == selectedAvailableMoveIndex ? Color.yellow : Color.white;
            graphicsHandler.drawFilledRectangleWithBorder(x, y, cardWidth, 62, fillColor, borderColor, 3);

            String moveName = availableMoves[index].getName();
            FontMetrics moveMetrics = graphics.getFontMetrics(moveFont);
            if (unlocked) {
                graphicsHandler.drawString(moveName,
                        x + (cardWidth - moveMetrics.stringWidth(moveName)) / 2,
                        y + (62 + moveMetrics.getAscent() - moveMetrics.getDescent()) / 2,
                        moveFont, alreadyEquipped ? Color.gray : Color.white);
            } else {
                graphicsHandler.drawString(moveName,
                        x + (cardWidth - moveMetrics.stringWidth(moveName)) / 2,
                        y + 26, moveFont, Color.gray);
                Font lockedFont = new Font("Arial", Font.PLAIN, 14);
                FontMetrics lockedMetrics = graphics.getFontMetrics(lockedFont);
                graphicsHandler.drawString("Locked",
                        x + (cardWidth - lockedMetrics.stringWidth("Locked")) / 2,
                        y + 48, lockedFont, Color.gray);
            }
        }

        drawMoveDescription(graphicsHandler, graphics, availableMoves[selectedAvailableMoveIndex]);

        String instructions = "WASD: Select a move    Enter: Equip    Escape: Back";
        FontMetrics instructionsMetrics = graphics.getFontMetrics(instructionsFont);
        graphicsHandler.drawString(instructions,
                (ScreenManager.getScreenWidth() - instructionsMetrics.stringWidth(instructions)) / 2,
                ScreenManager.getScreenHeight() - 24, instructionsFont, Color.white);
    }

    private void drawMoveDescription(GraphicsHandler graphicsHandler, Graphics2D graphics, Move move) {
        Font descriptionFont = new Font("Arial", Font.PLAIN, 16);
        FontMetrics descriptionMetrics = graphics.getFontMetrics(descriptionFont);
        String[] descriptionLines = wrapText(move.getDescription(), descriptionMetrics, 740);
        int lineY = 520;
        for (String line : descriptionLines) {
            graphicsHandler.drawString(line,
                    (ScreenManager.getScreenWidth() - descriptionMetrics.stringWidth(line)) / 2,
                    lineY, descriptionFont, Color.lightGray);
            lineY += descriptionMetrics.getHeight();
        }
    }

    private String[] wrapText(String text, FontMetrics metrics, int maxWidth) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (line.length() > 0 && metrics.stringWidth(candidate) > maxWidth) {
                lines.add(line.toString());
                line.setLength(0);
            }
            if (line.length() > 0) {
                line.append(' ');
            }
            line.append(word);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines.toArray(new String[0]);
    }
}