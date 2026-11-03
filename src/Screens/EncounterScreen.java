package Screens;

import Battle.Move;
import Battle.Haymaker;
import Engine.*;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import Level.Player;

// This class is for the encounter screen
public class EncounterScreen extends Screen {
    private static final int RESULT_SCREEN_DELAY = 3 * Config.TARGET_FPS;
    private static final int BURN_MESSAGE_DELAY = Config.TARGET_FPS;

    protected SpriteFont encounterMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;
    protected SpriteFont moveList;
    protected int movePointerLocationX, movePointerLocationY;
    protected int battleCurrentMenuItemX = 0;
    protected int battleCurrentMenuItemY = 0;
    protected int battleMenuItemSelected = -1;
    protected int battleKeyPressTimer;
    protected SpriteFont playerPlaceholder;
    protected SpriteFont enemyPlaceholder;
    protected int enemyHp, enemyMaxHp, playerHp, playerMaxHp;
    protected int usedMoves1 = 0, usedMoves2 = 0, usedMoves3 = 0, usedMoves4 = 0;
    protected int damageRoll = 0;
    protected int damageCalcPlayer = 0;
    protected int damageCalcEnemy = 0;
    protected int playerDefenseBuff = 0;
    protected int enemyDefenseBuff = 0;
    protected int playerAttackBuff = 0;
    protected int enemyAttackBuff = 0;
    protected boolean playerStunned = false;
    protected boolean enemyStunned = false;
    protected int stunChance = 50;
    protected boolean moveStun = false;
    protected boolean playerProtected = false;
    protected boolean enemyProtected = false;
    protected boolean playerBurned = false;
    protected boolean enemyBurned = false;
    protected boolean playerBurnProcessedThisTurn;
    protected boolean enemyBurnProcessedThisTurn;
    protected String playerTurnBurnMessage = "";
    protected String lastEnemyTurnMessage = "";
    protected int playerBurnMessageDelay;
    protected int lifestealAmount;
    protected boolean isChargingPlayer = false;
    protected boolean isChargingEnemy = false;
    protected boolean usedHaymakerPlayer = false;
    protected boolean usedHaymakerEnemy = false;
    protected int resultScreenTimer;
    protected SpriteFont playerLabel;
    protected SpriteFont enemyLabel;
    protected SpriteFont[] buttonLabels = new SpriteFont[4];
    protected Move[] moves;
    protected Move[] enemyMoves;
    protected int textBoxTimer;
    protected int enemyTurnDelay;
    protected boolean enemyTurnPending;
    protected boolean enemyStarts;
    protected String battleMessage;
    protected SpriteFont moveInfoText;
    protected Random random = new Random();
    protected BufferedImage knightBack;
    protected BufferedImage enemySprite;
    protected int playerAttack = 10;
    protected int enemyAttack = 10;
    protected int expReward = 20;
    protected boolean expAwarded;
    protected String expMessage = "";
    protected Move animatingMove;
    protected boolean animatingPlayerIsUser;
    protected int animationFrame;
    protected boolean animatingCharge;

    public EncounterScreen(PlayLevelScreen playLevelScreen, Move[] enemyMoves) {
        this.playLevelScreen = playLevelScreen;
        this.enemyMoves = enemyMoves.clone();
        initialize();
        knightBack = ImageLoader.load("KnightBack.png");
    }

    public void setMonsterName(String mName){
        initialize();
        enemyPlaceholder.setText(mName + " will go here");
    }

    // damage for one hit. attack 10 is "normal", so a level 1 player hits exactly as hard as before
    private int calculateDamage(int power, int attackerAttack, int attackerBuff, int defenderBuff) {
        damageRoll = (int)(Math.random() * 10) - 5;
        int damage = (power * attackerAttack) / 10 + damageRoll + attackerBuff - defenderBuff;
        return Math.max(1, damage);
    }
    private String useMove(Move move, boolean playerAttacking) {
        if (random.nextInt(100) >= move.getAccuracy()) {
            if (move.getType().equals("Crash Attack")) {
                // missing a crash move hurts the user instead
                if (playerAttacking) {
                    playerHp = Math.max(0, playerHp - move.getEffect());
                } else {
                    enemyHp = Math.max(0, enemyHp - move.getEffect());
                }
                return move.getName() + " missed! The user crashed and took " + move.getEffect() + " damage!";
            }
            return move.getName() + " missed!";
        }
        startAnimation(move, playerAttacking);

        if (playerAttacking) {
            usedHaymakerPlayer = false;
            if (isChargingPlayer && playerAttacking) {
                isChargingPlayer = false;
                usedHaymakerPlayer = true;
                startAnimation(new Haymaker(), true);
                enemyHp = Math.max(0, enemyHp - (((80 + damageRoll) + playerAttackBuff) - enemyDefenseBuff));
                return "Haymaker hit for " + (((80 + damageRoll) + playerAttackBuff) - enemyDefenseBuff) + " damage!";
            }
            if (playerStunned == true) {
                if (random.nextInt(100) < stunChance) {
                    animatingMove = null;
                    return "Player is stunned!";
                } else {
                    playerStunned = false;
                }
            }
            if (move.getPower() > 0) {
                if (enemyProtected == true) {
                    enemyProtected = false;
                    return "Protection nullified damage!";
                }
                if (move.getType().equals("Charge Attack") && !usedHaymakerPlayer) {
                    if (!isChargingPlayer) {
                        isChargingPlayer = true;
                        startChargeAnimation(move, true);
                        return move.getName() + " is charging!";
                    }
                }
                damageCalcPlayer = calculateDamage(move.getPower(), playerAttack, playerAttackBuff, enemyDefenseBuff);
                enemyHp = Math.max(0, enemyHp - damageCalcPlayer);

                if (move.getType().equals("Electric Attack")) {
                    if (random.nextInt(100) < stunChance) {
                        moveStun = true;
                        enemyStunned = true;
                    }
                } else if (move.getType().equals("Vamp")) {
                    lifestealAmount = (damageCalcPlayer) / 2;
                    if (lifestealAmount < 0) {
                        lifestealAmount = 1;
                    }
                    enemyHp = Math.min(enemyMaxHp, enemyHp + lifestealAmount);
                } else if (move.getType().equals("Def Boost Attack")) {
                    playerDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Atk Lower Attack")) {
                    enemyAttackBuff += Math.abs(move.getEffect());
                }
            } else {
                if (enemyProtected == true) {
                    enemyProtected = false;
                }
                if (move.getType().equals("Defense Buff")) {
                    playerDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Attack Buff")) {
                    playerAttackBuff += move.getEffect();
                } else if (move.getType().equals("Healing")) {
                    playerHp = Math.min(playerMaxHp, playerHp + move.getEffect());
                } else if (move.getType().equals("Attack Debuff")) {
                    enemyAttackBuff -= Math.abs(move.getEffect());
                } else if (move.getName().equals("Defense Debuff")) {
                    enemyDefenseBuff -= Math.abs(move.getEffect());
                } else if (move.getType().equals("Stun")) {
                    enemyStunned = true;
                } else if (move.getType().equals("Protect")) {
                    playerProtected = true;
                } else if (move.getType().equals("Burn")) {
                    enemyBurned = true;
                }
            }
        } else {
            usedHaymakerEnemy = false;
            if (isChargingEnemy && !playerAttacking) {
                isChargingEnemy = false;
                usedHaymakerEnemy = true;
                startAnimation(new Haymaker(), false);
                playerHp = Math.max(0, playerHp - (((80 + damageRoll) + enemyAttackBuff) - playerDefenseBuff));
                return "Haymaker hit for " + (((80 + damageRoll) + enemyAttackBuff) - playerDefenseBuff) + " damage!";
            }
            if (enemyStunned == true) {
                if (random.nextInt(100) < stunChance) {
                    animatingMove = null;
                    return "Enemy is stunned!";
                } else {
                    enemyStunned = false;
                }
            }
            if (move.getPower() > 0) {
                if (playerProtected == true) {
                    playerProtected = false;
                    return "Protection nullified damage!";
                }
                if (move.getType().equals("Charge Attack") && !usedHaymakerEnemy) {
                    if (!isChargingEnemy) {
                        isChargingEnemy = true;
                        startChargeAnimation(move, false);
                        return move.getName() + " is charging!";
                    }
                }
                damageCalcEnemy = calculateDamage(move.getPower(), enemyAttack, enemyAttackBuff, playerDefenseBuff);
                playerHp = Math.max(0, playerHp - damageCalcEnemy);
                if (move.getType().equals("Electric Attack")) {
                    if (random.nextInt(100) < stunChance) {
                        moveStun = true;
                        playerStunned = true;
                    }
                } else if (move.getType().equals("Vamp")) {
                    lifestealAmount = (damageCalcEnemy) / 2;
                    if (lifestealAmount < 0) {
                        lifestealAmount = 1;
                    }
                    enemyHp = Math.min(enemyMaxHp, enemyHp + lifestealAmount);
                } else if (move.getType().equals("Def Boost Attack")) {
                    enemyDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Atk Lower Attack")) {
                    playerAttackBuff += Math.abs(move.getEffect());
                }
            } else {
                if (playerProtected == true) {
                    playerProtected = false;
                }
                if (move.getType().equals("Defense Buff")) {
                    enemyDefenseBuff += move.getEffect();
                } else if (move.getType().equals("Attack Buff")) {
                    enemyAttackBuff += move.getEffect();
                } else if (move.getType().equals("Defense Debuff")) {
                    playerDefenseBuff -= Math.abs(move.getEffect());
                } else if (move.getType().equals("Attack Debuff")) {
                    playerAttackBuff -= Math.abs(move.getEffect());
                } else if (move.getType().equals("Healing")) {
                    enemyHp = Math.min(enemyMaxHp, enemyHp + move.getEffect());
                } else if (move.getType().equals("Stun")) {
                    playerStunned = true;
                } else if (move.getType().equals("Protect")) {
                    enemyProtected = true;
                } else if (move.getType().equals("Burn")) {
                    playerBurned = true;

                } else if (move.getType().equals("Healing")) {
                    enemyHp = Math.min(enemyMaxHp, enemyHp + move.getEffect());
                } else if (move.getType().equals("Attack Debuff")) {
                    playerAttackBuff += Math.abs(move.getEffect());
                } else if (move.getType().equals("Defense Debuff")) {
                    playerDefenseBuff += Math.abs(move.getEffect());
                } else if (move.getType().equals("Stun")) {
                    playerStunned = true;
                } else if (move.getType().equals("Protect")) {
                    enemyProtected = true;
                } else if (move.getType().equals("Burn")) {
                    playerBurned = true;
                }
            }
        }
        if (move.getPower() > 0 && playerAttacking) {
            if (damageCalcPlayer < 0) {
                return move.getName() + " hit for 1 damage!";
            }

            if (move.getType().equals("Electric Attack")) {
                if (moveStun) {
                    moveStun = false;
                    return move.getName() + " hit for " + (damageCalcPlayer) + " damage and stunned the enemy!";
                }
            } else if (move.getType().equals("Vamp")) {
                return move.getName() + " hit for " + (damageCalcPlayer) + " damage and healed for " + lifestealAmount + " HP!";
            } else if (move.getType().equals("Def Boost Attack")) {
                return move.getName() + " hit for " + (damageCalcPlayer) + " damage and increased user's defense by " + move.getEffect() + "!";
            } else if (move.getType().equals("Atk Lower Attack")) {
                return move.getName() + " hit for " + (damageCalcPlayer) + " damage and lowered enemy's attack by " + Math.abs(move.getEffect()) + "!";
            }
            
            return move.getName() + " hit for " + (damageCalcPlayer) + " damage!";
        }

        if (move.getPower() > 0 && !playerAttacking) {
            if (damageCalcEnemy < 0) {
                return move.getName() + " hit for 1 damage!";
            }

            if (move.getType().equals("Electric Attack")) {
                if (moveStun) {
                    moveStun = false;
                    return move.getName() + " hit for " + (damageCalcEnemy) + " damage and stunned the enemy!";
                }
            } else if (move.getType().equals("Vamp")) {
                return move.getName() + " hit for " + (damageCalcEnemy) + " damage and healed for " + lifestealAmount + " HP!";
            } else if (move.getType().equals("Def Boost Attack")) {
                return move.getName() + " hit for " + (damageCalcEnemy) + " damage and increased user's defense by " + move.getEffect() + "!";
            } else if (move.getType().equals("Atk Lower Attack")) {
                return move.getName() + " hit for " + (damageCalcEnemy) + " damage and lowered enemy's attack by " + Math.abs(move.getEffect()) + "!";
            }

            return move.getName() + " hit for " + (damageCalcEnemy) + " damage!";
        }

        if (move.getType().equals("Defense Buff")) {
            return move.getName() + " increased user's defense by " + move.getEffect() + "!";
        }
        if (move.getType().equals("Attack Buff")) {
            return move.getName() + " increased user's attack by " + move.getEffect() + "!";
        }
        if (move.getType().equals("Healing")) {
            return move.getName() + " restored " + Math.abs(move.getEffect()) + " HP!";
        }
        if (move.getType().equals("Attack Debuff")) {
            return move.getName() + " decreased enemy's attack by " + Math.abs(move.getEffect()) + "!";
        }
        if (move.getType().equals("Defense Debuff")) {
            return move.getName() + " decreased enemy's defense by " + Math.abs(move.getEffect()) + "!";
        }
        if (move.getType().equals("Stun")) {
            return move.getName() + " stunned the enemy!";
        }
        if (move.getType().equals("Protect")) {
            return move.getName() + " prevents damage next turn!";
        }
        if (move.getType().equals("Burn")) {
            return move.getName() + " burned the enemy!";
        }
        return move.getName() + " had no effect!";
    }

    private int getUsedMoves(int moveIndex) {
        switch (moveIndex) {
            case 0:
                return usedMoves1;
            case 1:
                return usedMoves2;
            case 2:
                return usedMoves3;
            case 3:
                return usedMoves4;
            default:
                throw new IllegalArgumentException("Invalid move index: " + moveIndex);
        }
    }

    private void startAnimation(Move move, boolean playerIsUser){
        animatingCharge = false;
        animatingMove = move;
        animatingPlayerIsUser = playerIsUser;
        animationFrame = 0;
    }

    private void startChargeAnimation(Move move, boolean playerIsUser) {
    startAnimation(move, playerIsUser);
    animatingCharge = true;
    }
    private void recordMoveUse(int moveIndex) {
        switch (moveIndex) {
            case 0:
                usedMoves1++;
                break;
            case 1:
                usedMoves2++;
                break;
            case 2:
                usedMoves3++;
                break;
            case 3:
                usedMoves4++;
                break;
            default:
                throw new IllegalArgumentException("Invalid move index: " + moveIndex);
        }
    }

    private boolean allMovesOutOfUses() {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i].getUses() - getUsedMoves(i) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize() {
        moves = playLevelScreen.getEquippedMoves();
        battleCurrentMenuItemX = 0;
        battleCurrentMenuItemY = 0;
        enemyStarts = random.nextBoolean();
        enemyTurnDelay = enemyStarts ? 60 : 0;
        enemyTurnPending = enemyStarts;
        battleMessage = enemyStarts ? "Enemy moves first!" : "";
        textBoxTimer = enemyStarts ? 60 : 0;
        battleMenuItemSelected = enemyStarts ? 0 : -1;
        moveInfoText = new SpriteFont("...", 35, 405, "Arial", 22, Color.white);
        instructions = new SpriteFont("Press enter to return", 120, 279,"Arial", 20, Color.white);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.S);
        keyLocker.lockKey(Key.A);
        keyLocker.lockKey(Key.D);
        keyLocker.lockKey(Key.W);
        keyLocker.lockKey(Key.SPACE);
        playerPlaceholder = new SpriteFont("Player will go here", 95, 400, "Arial", 24, Color.black);
        enemyPlaceholder = new SpriteFont("Enemy will go here", 480, 230, "Arial", 24, Color.black);
        playerLabel = new SpriteFont("Player", 10, ScreenManager.getScreenHeight() - 60, "Arial",24, Color.white);
        enemyLabel = new SpriteFont("Enemy", ScreenManager.getScreenWidth() - 85, 45, "Arial", 24, Color.white);
        enemyHp = 100;
        enemyMaxHp = 100;
        Player player = playLevelScreen.getPlayer();
        playerMaxHp = player.getMaxHp();
        playerHp = playerMaxHp;
        playerAttack = player.getAttack();
        playerLabel.setText("Player  Lv " + player.getLevel());
        expAwarded = false;
        expMessage = "";
        usedMoves1 = 0;
        usedMoves2 = 0;
        usedMoves3 = 0;
        usedMoves4 = 0;
        damageRoll = 0;
        playerDefenseBuff = 0;
        enemyDefenseBuff = 0;
        playerAttackBuff = 0;
        enemyAttackBuff = 0;
        playerStunned = false;
        enemyStunned = false;
        playerProtected = false;
        enemyProtected = false;
        playerBurned = false;
        enemyBurned = false;
        playerBurnProcessedThisTurn = false;
        enemyBurnProcessedThisTurn = false;
        lifestealAmount = 0;
        isChargingPlayer = false;
        isChargingEnemy = false;
        playerTurnBurnMessage = "";
        lastEnemyTurnMessage = "";
        playerBurnMessageDelay = 0;
        resultScreenTimer = 0;
        for(int row = 0; row < 2; row ++){
            for(int col = 0; col < 2; col ++){
                int bx = 440 + col * 160;
                int by = 395 + row * 70;
                SpriteFont label = new SpriteFont(moves[row * 2 + col].getName(), bx + 30, by + 15, "Arial", 28, Color.white);
                label.setFontStyle(Font.BOLD);
                label.setOutlineColor(Color.blue);
                label.setOutlineThickness(3);
                buttonLabels[row * 2 + col] = label;
            }
        }
        animatingMove = null;
    }

    @Override
    public void update() {
        if(animatingMove != null){
            animationFrame ++;
            if(animationFrame >= Move.getAnimationLength()){
                animatingMove = null;
            }
        }
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (enemyHp > 0 && playerHp > 0
                && Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
            keyLocker.lockKey(Key.ENTER);
            enemyHp = 0;
            resultScreenTimer = 0;
            return;
        }
        if (enemyHp <= 0 || playerHp <= 0) {
            if(!expAwarded){
                awardExp();
            }
            if (resultScreenTimer > 0) {
                keyLocker.lockKey(Key.ENTER);
                resultScreenTimer--;
                return;
            }
            if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {
                keyLocker.lockKey(Key.ENTER);
                playLevelScreen.returnFromBossFight(playerHp <= 0);
                playLevelScreen.returnFromEncounter(playerHp <= 0);
            }
            return;
        }
        if(animatingMove != null){
            return;
        }

        if (enemyTurnPending && enemyTurnDelay > 0) {
            enemyTurnDelay--;
            if (enemyTurnDelay == 0 && enemyHp > 0 && playerHp > 0) {
                String enemyBurnMessage = "";
                if (enemyBurned && !enemyBurnProcessedThisTurn) {
                    enemyHp = Math.max(0, enemyHp - 5);
                    enemyBurnMessage = "Enemy took 5 damage from the burn!";
                    enemyBurnProcessedThisTurn = true;
                    lastEnemyTurnMessage = enemyBurnMessage;
                    battleMessage = enemyBurnMessage;
                    moveInfoText.setText(battleMessage);
                    textBoxTimer = 240;
                    if (enemyHp <= 0) {
                        enemyTurnPending = false;
                        startResultScreenDelay();
                        battleMessage = enemyBurnMessage + "\n" + battleMessage;
                        moveInfoText.setText(battleMessage);
                        return;
                    }
                    enemyTurnDelay = BURN_MESSAGE_DELAY;
                    return;
                }
                Move enemyMove = enemyMoves[random.nextInt(enemyMoves.length)];
                String enemyMoveMessage = useMove(enemyMove, false);
                enemyTurnPending = false;
                lastEnemyTurnMessage = enemyBurnMessage.isEmpty()
                        ? enemyMoveMessage
                        : enemyBurnMessage + "\n" + enemyMoveMessage;
                if (!enemyBurnMessage.isEmpty()) {
                    battleMessage = enemyBurnMessage;
                }
                battleMessage += "\n" + enemyMoveMessage;
                moveInfoText.setText(battleMessage);
                textBoxTimer = 240;
                startResultScreenDelay();
                if (!enemyBurnMessage.isEmpty() && (enemyHp <= 0 || playerHp <= 0)) {
                    battleMessage = enemyBurnMessage + "\n" + battleMessage;
                    moveInfoText.setText(battleMessage);
                }
                enemyBurnProcessedThisTurn = false;
                if (playerHp <= 0) {
                    return;
                }
                if (playerBurned) {
                    playerBurnMessageDelay = BURN_MESSAGE_DELAY;
                    return;
                }
            }
        }

        if (playerBurnMessageDelay > 0) {
            playerBurnMessageDelay--;
            return;
        }

        if (enemyTurnDelay == 0 && !playerBurnProcessedThisTurn) {
            playerBurnProcessedThisTurn = true;
            if (playerBurned == true) {
                playerHp = Math.max(0, playerHp - 5);
                playerTurnBurnMessage = "You took 5 damage from the burn!";
                battleMessage = (lastEnemyTurnMessage.isEmpty()
                        ? ""
                        : lastEnemyTurnMessage + "\n") + playerTurnBurnMessage;
                moveInfoText.setText(battleMessage);
                textBoxTimer = 240;
                startResultScreenDelay();
                if (playerHp <= 0) {
                    battleMessage = playerTurnBurnMessage + "\n" + battleMessage;
                    moveInfoText.setText(battleMessage);
                    return;
                }
            }
        }

        if(Keyboard.isKeyUp(Key.S)){
            keyLocker.unlockKey(Key.S);
        }
        if(Keyboard.isKeyDown(Key.S) && !keyLocker.isKeyLocked(Key.S)){
            //move cursor down here
            battleCurrentMenuItemY = 1;
            keyLocker.lockKey(Key.S);
        }
        if(Keyboard.isKeyUp(Key.W)){
            keyLocker.unlockKey(Key.W);
        }
        if(Keyboard.isKeyDown(Key.W) && !keyLocker.isKeyLocked(Key.W)){
            //move cursor up here
            battleCurrentMenuItemY = 0;
            keyLocker.lockKey(Key.W);
        }
        if(Keyboard.isKeyUp(Key.A)){
            keyLocker.unlockKey(Key.A);
        }
        if(Keyboard.isKeyDown(Key.A) && !keyLocker.isKeyLocked(Key.A)){
            //move cursor left here
            battleCurrentMenuItemX = 0;
            keyLocker.lockKey(Key.A);
        }
        if(Keyboard.isKeyUp(Key.D)){
            keyLocker.unlockKey(Key.D);
        }
        if(Keyboard.isKeyDown(Key.D) && !keyLocker.isKeyLocked(Key.D)){
            //move cursor right here
            battleCurrentMenuItemX = 1;
            keyLocker.lockKey(Key.D);
        }

        if(Keyboard.isKeyUp(Key.SPACE)){
            keyLocker.unlockKey(Key.SPACE);
        }
        if(Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)){
            keyLocker.lockKey(Key.SPACE);
            if (enemyTurnDelay == 0 && enemyHp > 0 && playerHp > 0) {
                battleMenuItemSelected = battleCurrentMenuItemY * 2 + battleCurrentMenuItemX;

                Move selectedMove = moves[battleMenuItemSelected];
                String turnBurnMessage = playerTurnBurnMessage;
                if (allMovesOutOfUses()) {
                    battleMessage = "\nAll of your moves are out of uses!";
                    damageRoll = (int)(Math.random() * 10) - 5;
                    if ((((20 + damageRoll) + playerAttackBuff) - enemyDefenseBuff) < 0) {
                        enemyHp = Math.max(0, enemyHp - 1);
                    } else {
                        enemyHp = Math.max(0, enemyHp - (((20 + damageRoll) + playerAttackBuff) - enemyDefenseBuff));
                    }
                    battleMessage += "\nStruggle hit for " + (((20 + damageRoll) + playerAttackBuff) - enemyDefenseBuff) + " damage!";
                    enemyTurnDelay = 60;
                    enemyTurnPending = true;
                    enemyBurnProcessedThisTurn = false;
                    playerBurnProcessedThisTurn = false;
                } else if (selectedMove.getUses() - getUsedMoves(battleMenuItemSelected) <= 0) {
                    battleMessage = (turnBurnMessage.isEmpty()
                            ? ""
                            : (lastEnemyTurnMessage.isEmpty()
                                    ? ""
                                    : lastEnemyTurnMessage + "\n") + turnBurnMessage + "\n")
                            + selectedMove.getName() + " has no uses left!";
                } else {
                    recordMoveUse(battleMenuItemSelected);
                    battleMessage = (turnBurnMessage.isEmpty()
                            ? ""
                            : (lastEnemyTurnMessage.isEmpty()
                                    ? ""
                                    : lastEnemyTurnMessage + "\n") + turnBurnMessage + "\n")
                            + useMove(selectedMove, true);
                    playerTurnBurnMessage = "";
                    if (enemyHp > 0) {
                        enemyTurnDelay = 60;
                        enemyTurnPending = true;
                        enemyBurnProcessedThisTurn = false;
                        playerBurnProcessedThisTurn = false;
                    }
                }

                moveInfoText.setText(battleMessage);
                textBoxTimer = 240;
                startResultScreenDelay();
                if (enemyHp <= 0) {
                    if (!turnBurnMessage.isEmpty()) {
                        battleMessage = turnBurnMessage + "\n" + battleMessage;
                        moveInfoText.setText(battleMessage);
                    }
                    return;
                }
            }
        }
        //to make the text box disappear after a set time
        if(textBoxTimer > 0){
                textBoxTimer --;
                if(textBoxTimer == 0){
                battleMenuItemSelected = -1;
                }
            }
    }

        // called once when the battle ends. full EXP for a win, a quarter for a loss
    private void awardExp() {
        expAwarded = true;
        Player player = playLevelScreen.getPlayer();
        int earned = enemyHp <= 0 ? expReward : expReward / 4;
        int oldMaxHp = player.getMaxHp();
        int oldAttack = player.getAttack();
        int levelsGained = player.gainExp(earned);

        expMessage = "+" + earned + " EXP";
        if (levelsGained > 0) {
            expMessage += "\nLevel up! You are now level " + player.getLevel() + "!"
                    + "\nMax HP " + oldMaxHp + " -> " + player.getMaxHp()
                    + "   Attack " + oldAttack + " -> " + player.getAttack();
        }
        expMessage += "\nEXP: " + player.getExp() + " / " + player.getExpToNextLevel();
    }

    private void startResultScreenDelay() {
        if (enemyHp <= 0 || playerHp <= 0) {
            resultScreenTimer = RESULT_SCREEN_DELAY;
            battleMessage = enemyHp <= 0
                    ? "The enemy has been defeated!"
                    : "The player has been defeated!";
            moveInfoText.setText(battleMessage);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (enemyHp <= 0 && resultScreenTimer <= 0) {
            drawVictoryScreen(graphicsHandler);
            return;
        }

        if (playerHp <= 0 && resultScreenTimer <= 0) {
            drawLoseScreen(graphicsHandler);
            return;
        }

        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.blue);
       Graphics2D g = graphicsHandler.getGraphics();
        g.setColor(new Color(250, 230, 90));
        g.fillOval(40, 40, 80, 80);  
        g.setColor(new Color(100, 200, 100));
        g.fillOval(-350, 150, 1000, 700);
        g.setColor(new Color(125, 225, 100));
        g.fillOval(200,150,1000,500);
        g.setColor(new Color(170, 250, 100)); 
        g.fillOval(-100,150,1000,600);
        g.setColor(Color.black);
        g.fillOval(40, 420, 310, 120);       // outer black ring
        g.setColor(new Color(80, 80, 80));
        g.fillOval(110, 445, 170, 60);       // inner gray part
        g.setColor(Color.black);
        g.fillOval(450, 260, 300, 100);
        g.setColor(Color.gray);
        g.fillOval( 510, 275, 160, 50);
        int barWidth = ScreenManager.getScreenWidth() - 20;
        int enemyFill = (int) (barWidth * ((double) enemyHp / enemyMaxHp));
        int barHeight = ScreenManager.getScreenHeight() - 24;
        graphicsHandler.drawFilledRectangleWithBorder(10, 10, barWidth, 14, Color.darkGray, Color.black, 2);
        graphicsHandler.drawFilledRectangle(10, 10, enemyFill, 14, new Color(120, 230, 70));
        int playerFill = (int) (barWidth * ((double) playerHp / playerMaxHp));
        graphicsHandler.drawFilledRectangleWithBorder(10, barHeight, barWidth, 14, Color.darkGray, Color.black, 2);
        graphicsHandler.drawFilledRectangle(10, barHeight, playerFill, 14, new Color(120, 230, 70));
        playerLabel.draw(graphicsHandler);
        enemyLabel.draw(graphicsHandler);
        //playerPlaceholder.draw(graphicsHandler);
        int knightX = 115, knightY = 315, enemyX = 500, enemyY = 150;
boolean hideKnight = false, hideEnemy = false;

if (animatingMove != null && animatingMove.getPower() > 0 && !animatingCharge) {
    double p = (double) animationFrame / Move.getAnimationLength();

    // attacker: forward for the first 15%, back by 30%
    double lunge = p < 0.15 ? p / 0.15 : (p < 0.3 ? (0.3 - p) / 0.15 : 0);
    int dx = (int) (30 * lunge);

    // target: shake left/right and blink between 30% and 70%
    boolean hit = p > 0.3 && p < 0.7;
    int shake = hit ? (animationFrame % 4 < 2 ? 6 : -6) : 0;
    boolean blink = hit && animationFrame % 6 < 2;

    if (animatingPlayerIsUser) {
        knightX += dx;  knightY -= dx / 2;   // up and right, toward the enemy
        enemyX += shake; hideEnemy = blink;
    } else {
        enemyX -= dx;   enemyY += dx / 2;    // down and left, toward the knight
        knightX += shake; hideKnight = blink;
    }
}

if (!hideKnight) {
    graphicsHandler.drawImage(knightBack, knightX, knightY, 160, 160);
}
if (!hideEnemy) {
    if (enemySprite != null) {
        graphicsHandler.drawImage(enemySprite, enemyX, enemyY, 192, 192);
    } else {
        enemyPlaceholder.draw(graphicsHandler);
    }
}
        //for the animations
        Stroke oldStroke = g.getStroke();
        Composite oldComposite = g.getComposite();
        if (animatingMove != null) {
        int px = 195, py = 395, ex = 596, ey = 246;
        int ux = animatingPlayerIsUser ? px : ex, uy = animatingPlayerIsUser ? py : ey;
        int tx = animatingPlayerIsUser ? ex : px, ty = animatingPlayerIsUser ? ey : py;
        if (animatingCharge) {
        animatingMove.drawChargeAnimation(g, animationFrame, ux, uy);
        } else {
        animatingMove.drawAnimation(g, animationFrame, ux, uy, tx, ty);
        }
}
g.setStroke(oldStroke);
g.setComposite(oldComposite);
        //for the buttons
        for(int row = 0; row < 2; row ++){
            for(int col = 0; col < 2; col ++){
                int bx = 440 + col * 160;
                int by = 395 + row * 70;
                Color borderColor = Color.black;
                if(battleCurrentMenuItemX == col && battleCurrentMenuItemY == row){
                    borderColor = Color.yellow;
                }
                graphicsHandler.drawFilledRectangleWithBorder(bx, by, 145, 55, new Color(200, 50, 40), borderColor, 3);
                SpriteFont label = buttonLabels[row * 2 + col];
                Font buttonFont = label.getFont().deriveFont(28f);
                int fontSize = 28;
                while (fontSize > 8
                        && g.getFontMetrics(buttonFont).stringWidth(label.getText()) > 125) {
                    fontSize--;
                    buttonFont = label.getFont().deriveFont((float) fontSize);
                }
                FontMetrics labelMetrics = g.getFontMetrics(buttonFont);
                label.setFont(buttonFont);
                label.setLocation(
                        bx + (145 - labelMetrics.stringWidth(label.getText())) / 2f,
                        by + (55 - labelMetrics.getHeight()) / 2f);
                label.draw(graphicsHandler);
            }
        }
        boolean showingMoveDescription = battleMenuItemSelected == -1
                && resultScreenTimer <= 0 && playerTurnBurnMessage.isEmpty();
        String textToDisplay = battleMessage;
        if (showingMoveDescription) {
            int selectedMoveIndex = battleCurrentMenuItemY * 2 + battleCurrentMenuItemX;
            Move selectedMove = moves[selectedMoveIndex];
            textToDisplay = selectedMove.getDescription() + "\nUses Left: "
                    + (selectedMove.getUses() - getUsedMoves(selectedMoveIndex));
        }
        if(showingMoveDescription || !playerTurnBurnMessage.isEmpty()
                || battleMenuItemSelected != -1 || resultScreenTimer > 0){
            graphicsHandler.drawFilledRectangleWithBorder(20, 395, 400, 125, new Color(30,30,60), Color.white, 3);
            moveInfoText.setText(fitTextToBox(textToDisplay, g));
            moveInfoText.drawWithParsedNewLines(graphicsHandler, 6);
        }
        //instructions.draw(graphicsHandler);
    }

    private String fitTextToBox(String text, Graphics2D graphics) {
        int fontSize = 22;
        while (true) {
            Font font = new Font("Arial", Font.PLAIN, fontSize);
            FontMetrics metrics = graphics.getFontMetrics(font);
            String wrappedText = wrapText(text, metrics, 370);
            int maxLines = 1 + Math.max(0,
                    (105 - metrics.getHeight()) / (fontSize + 6));
            if (wrappedText.split("\n", -1).length <= maxLines || fontSize == 1) {
                moveInfoText.setFont(font);
                return wrappedText;
            }
            fontSize--;
        }
    }

    private String wrapText(String text, FontMetrics metrics, int maxWidth) {
        StringBuilder wrappedText = new StringBuilder();
        String[] paragraphs = text.split("\\n", -1);
        for (int paragraphIndex = 0; paragraphIndex < paragraphs.length; paragraphIndex++) {
            if (paragraphIndex > 0) {
                wrappedText.append('\n');
            }
            StringBuilder line = new StringBuilder();
            for (String word : paragraphs[paragraphIndex].split("\\s+")) {
                if (word.isEmpty()) {
                    continue;
                }
                if (line.length() > 0 && metrics.stringWidth(line + " " + word) > maxWidth) {
                    wrappedText.append(line).append('\n');
                    line.setLength(0);
                }
                while (metrics.stringWidth(word) > maxWidth) {
                    int splitIndex = 1;
                    while (splitIndex < word.length()
                            && metrics.stringWidth(word.substring(0, splitIndex + 1)) <= maxWidth) {
                        splitIndex++;
                    }
                    if (line.length() > 0) {
                        wrappedText.append(line).append('\n');
                        line.setLength(0);
                    }
                    wrappedText.append(word, 0, splitIndex).append('\n');
                    word = word.substring(splitIndex);
                }
                if (line.length() > 0) {
                    line.append(' ');
                }
                line.append(word);
            }
            wrappedText.append(line);
        }
        return wrappedText.toString();
    }

    private void drawVictoryScreen(GraphicsHandler graphicsHandler) {
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();
        graphicsHandler.drawFilledRectangle(0, 0, screenWidth, screenHeight, Color.black);

        Graphics2D graphics = graphicsHandler.getGraphics();
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        Font promptFont = new Font("Arial", Font.PLAIN, 24);
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        FontMetrics promptMetrics = graphics.getFontMetrics(promptFont);

        String title = "You Win";
        String prompt = "Press Enter to return to overworld";
        graphicsHandler.drawString(title, (screenWidth - titleMetrics.stringWidth(title)) / 2,
                screenHeight / 2, titleFont, Color.green);
        graphicsHandler.drawString(prompt, (screenWidth - promptMetrics.stringWidth(prompt)) / 2,
                screenHeight / 2 + 50, promptFont, Color.white);
        drawExpMessage(graphicsHandler, screenHeight / 2 + 100);
    }

        // draws expMessage centered, one line at a time, starting at y
    private void drawExpMessage(GraphicsHandler graphicsHandler, int y) {
        Font expFont = new Font("Arial", Font.PLAIN, 22);
        FontMetrics metrics = graphicsHandler.getGraphics().getFontMetrics(expFont);
        for (String line : expMessage.split("\n")) {
            int x = (ScreenManager.getScreenWidth() - metrics.stringWidth(line)) / 2;
            graphicsHandler.drawString(line, x, y, expFont, Color.yellow);
            y += 30;
        }
    }
    private void drawLoseScreen(GraphicsHandler graphicsHandler) {
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();
        graphicsHandler.drawFilledRectangle(0, 0, screenWidth, screenHeight, Color.black);

        Graphics2D graphics = graphicsHandler.getGraphics();
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        Font promptFont = new Font("Arial", Font.PLAIN, 24);
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
        FontMetrics promptMetrics = graphics.getFontMetrics(promptFont);

        String title = "You Lose";
        String prompt = "Press Enter to return to overworld";
        graphicsHandler.drawString(title, (screenWidth - titleMetrics.stringWidth(title)) / 2,
                screenHeight / 2, titleFont, Color.red);
        graphicsHandler.drawString(prompt, (screenWidth - promptMetrics.stringWidth(prompt)) / 2,
                screenHeight / 2 + 50, promptFont, Color.white);
        drawExpMessage(graphicsHandler, screenHeight / 2 + 100);
    }
}