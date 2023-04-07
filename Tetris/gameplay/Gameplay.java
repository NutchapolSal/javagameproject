package Tetris.gameplay;

import Tetris.data.BlockWithConnection;
import Tetris.data.GoalData;
import Tetris.data.GuiData;
import Tetris.data.ObjectDataGrid;
import Tetris.data.PlayerRenderData;
import Tetris.data.mino.Mino;
import Tetris.gameplay.goal.Goal;
import Tetris.gameplay.goal.GoalState;
import Tetris.gameplay.goal.LineGoal;
import Tetris.gameplay.goal.NoGoal;
import Tetris.gameplay.goal.TimeGoal;
import Tetris.gameplay.randomizer.MinoRandomizer;
import Tetris.gameplay.randomizer.SevenBagRandomizer;
import Tetris.input.PlayerInput;
import Tetris.input.RawInputSource;
import Tetris.input.Rotation;
import Tetris.settings.GameplayMode;
import Tetris.settings.ReceiveSettings;
import Tetris.settings.SettingKey;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Gameplay implements ReceiveSettings {
    private final class GameLoopTask extends TimerTask {
        public void run() {
            long nowFrame = System.nanoTime();
            if (nowFrame - lastFrame < FRAME_DELAY) {
                return;
            }
            lastFrame += FRAME_DELAY;
            timeMillis = TimeUnit.NANOSECONDS.toMillis(nowFrame - startTime);
            goalState = goal.calculate(timeMillis, linesCleared);
            if (goalState != GoalState.NONE) {
                gameLoop.cancel();
                renderEnd();
                return;
            }

            pis[0].tick();

            if (pis[0].getHold() && !playerDatas[0].lockHold) {
                processHold();
            }

            if (pis[0].getXMove() != 0) {
                processXMove(0);
            }

            if (pis[0].getRotation() != Rotation.None) {
                processRotation(0);
            }

            if (pis[0].getHardDrop()) {
                processHardDrop();
            }
            playerDatas[0].softDropping = pis[0].getSoftDrop();
            if (playerDatas[0].softDropping) {
                processSoftDrop();
            }

            processGravity();

            boolean loopContinueFromLock = processLockDelay();
            if (!loopContinueFromLock) {
                gameLoop.cancel();
                renderEnd();
                return;
            }

            playerLockProgress = (double) playerDatas[0].lockDelayFrames / lockDelayMaxFrames;
            pdr = playfield.getPlayerRenderData();

            if (!playfield.hasPlayerMino()) {
                boolean loopContinueFromSpawn = processPieceSpawn();
                if (!loopContinueFromSpawn) {
                    gameLoop.cancel();
                    renderEnd();
                    return;
                }
            }

            renderFrame();
        }
    }

    private final class CountdownTask extends TimerTask {
        public void run() {
            countdownGui = countdown;
            renderFrame();
            if (countdown == 0) {
                startGameLoop();
                countdownTask.cancel();
                return;
            }
            countdown--;
        }
    }

    private static class PlayerData {
        final int playerIndex;
        final MinoRandomizer minoRandomizer;
        final Queue<Mino> nextQueue = new ArrayDeque<>();
        Mino hold = null;
        boolean lockHold = false;
        boolean lastMoveTSpin = false;
        double gravityCount = 0;
        int lockDelayFrames = 0;
        int lockResetCount = 0;
        int lowestPlayerY;
        boolean hardDropLock = false;
        String spinName = null;
        boolean spinMini = false;
        boolean softDropping = true;

        PlayerData(int playerIndex, MinoRandomizer minoRandomizer) {
            this.playerIndex = playerIndex;
            this.minoRandomizer = minoRandomizer;
            for (int i = 0; i < 5; i++) {
                nextQueue.offer(minoRandomizer.next());
            }
        }

        Mino getNextMino() {
            nextQueue.offer(minoRandomizer.next());
            var nextMino = nextQueue.poll();
            return nextMino;
        }

        Mino[] getNextQueueGuiData() {
            return nextQueue.toArray(new Mino[0]);
        }

        void resetLockDelay() {
            lockDelayFrames = 0;
            lockResetCount++;
        }

    }

    private static long FRAME_DELAY = 16_666_666;
    private int lockResetMaxCount = 15;
    private int lockDelayMaxFrames = 30;
    private boolean sonicDrop = false;

    private PlayerInput[] pis = new PlayerInput[] { new PlayerInput() };
    private Timer timer = new Timer();
    private TimerTask gameLoop;
    private TimerTask countdownTask;
    private Goal goal;
    private Queue<Goal> newGoal = new ArrayBlockingQueue<>(1);
    private long startTime;
    private long lastFrame;
    private long timeMillis;
    private SingleplayerPlayfield playfield;
    private PlayerData[] playerDatas = new PlayerData[1];
    private int linesCleared;
    private int level;
    private int score;
    private int b2bCount;
    private int comboCount;
    private int countdown;

    private boolean zenMode;

    private Queue<GuiData> renderQueue = new ArrayBlockingQueue<>(3);
    private PlayerRenderData pdr;
    private double playerLockProgress;
    private double windowNudgeX;
    private double windowNudgeY;
    private ObjectDataGrid<BlockWithConnection> renderBlocks;
    private Mino[] nextQueueGuiData = new Mino[6];
    private int calloutLines;
    private String spinNameGui;
    private boolean allCleared;
    private GoalData goalData;
    private GoalState goalState;
    private int countdownGui;
    private String gamemodeName = "";
    private boolean danger;

    public void setRawInputSource(int index, RawInputSource ris) {
        pis[index].setRawInputSource(ris);
    }

    public void startGame() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (gameLoop != null) {
            gameLoop.cancel();
        }

        gameLoop = new GameLoopTask();
        countdownTask = new CountdownTask();
        goal = newGoal.peek() != null ? newGoal.poll() : goal;
        lastFrame = System.nanoTime();
        timeMillis = 0;
        playfield = new SingleplayerPlayfield(10, 20);
        playerDatas[0] = new PlayerData(0,
                new SevenBagRandomizer(
                        System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(5)));
        linesCleared = 0;
        level = 1;
        score = 0;
        b2bCount = 0;
        comboCount = 0;
        countdown = 3;

        zenMode = goal instanceof NoGoal;
        level = zenMode ? 2 : level;

        pdr = playfield.getPlayerRenderData();
        playerLockProgress = 0;
        windowNudgeX = 0;
        windowNudgeY = 0;
        goalState = GoalState.NONE;
        renderBlocks = playfield.getRenderBlocks();
        calloutLines = 0;
        allCleared = false;
        goalData = goal.getGoalData();
        danger = false;

        nextQueueGuiData = playerDatas[0].getNextQueueGuiData();
        renderFrame();

        timer.scheduleAtFixedRate(countdownTask, 0, 750);
    }

    private void startGameLoop() {
        startTime = System.nanoTime();
        lastFrame = System.nanoTime();
        playfield.spawnPlayerMino(playerDatas[0].getNextMino());
        nextQueueGuiData = playerDatas[0].getNextQueueGuiData();
        timer.scheduleAtFixedRate(gameLoop, 0, 3);
    }

    private void processHold() {
        if (playerDatas[0].hold == null) {
            playerDatas[0].hold = playfield.swapHold(playerDatas[0].getNextMino());
            nextQueueGuiData = playerDatas[0].getNextQueueGuiData();
        } else {
            playerDatas[0].hold = playfield.swapHold(playerDatas[0].hold);
        }
        playerDatas[0].lockHold = true;
        playerDatas[0].lockResetCount = 0;
        playerDatas[0].lockDelayFrames = 0;
        playerDatas[0].gravityCount = 0;
        playerDatas[0].lowestPlayerY = playfield.getPlayerMinoY();
    }

    private void processXMove(int index) {
        if (playfield.moveXPlayerMino(pis[index].getXMove())) {
            playerDatas[0].resetLockDelay();
            playerDatas[0].lastMoveTSpin = false;
        } else {
            windowNudgeX += pis[index].getXMove() * 3;
        }
    }

    private void processRotation(int index) {
        playerDatas[0].lastMoveTSpin = false;
        playerDatas[0].spinMini = false;
        switch (playfield.rotatePlayerMino(pis[index].getRotation())) {
            case SuccessTSpinMini:
                playerDatas[0].spinMini = true;
                // fallthrough
            case SuccessTSpin:
                playerDatas[0].lastMoveTSpin = true;
                playerDatas[0].spinName = playfield.getPlayerMinoName();
                windowNudgeX += calculateRotationNudge(index);
                // fallthrough
            case SuccessTwist:
            case Success:
                playerDatas[0].resetLockDelay();
                // fallthrough
            case Fail:
                break;
        }
    }

    private int calculateRotationNudge(int index) {
        switch (pis[index].getRotation()) {
            case Clockwise:
                return 6;
            case CounterClockwise:
                return -6;
            case Flip:
                switch (playfield.getPlayerMinoDirection()) {
                    case Down:
                    case Right:
                        return 6;
                    default:
                        return -6;
                }
            default:
                return 0;
        }
    }

    private void processHardDrop() {
        int blocksMoved = playfield.sonicDropPlayerMino();
        if (blocksMoved != 0) {
            playerDatas[0].lastMoveTSpin = false;
        }
        playerDatas[0].hardDropLock = true;
        windowNudgeY += 6;
        score += 2 * blocksMoved;
    }

    private void processSoftDrop() {
        if (sonicDrop) {
            int blocksMoved = playfield.sonicDropPlayerMino();
            if (blocksMoved != 0) {
                resetLockCount();
            }
            score += blocksMoved;
        } else {
            playerDatas[0].gravityCount += getGravityFromLevel(level) * 6;
            playerDatas[0].softDropping = true;
        }
    }

    private void processGravity() {
        playerDatas[0].gravityCount += getGravityFromLevel(level);
        int dropCount = (int) playerDatas[0].gravityCount;
        for (int i = 0; i < dropCount; i++) {
            if (playfield.moveYPlayerMino(-1)) {
                resetLockCount();
                playerDatas[0].lastMoveTSpin = false;
            } else {
                break;
            }
        }
        playerDatas[0].gravityCount -= dropCount;
        if (playerDatas[0].softDropping) {
            score += dropCount;
        }
    }

    /**
     * @return true if gameloop should not end, false if should end
     */
    private boolean processLockDelay() {
        if (playfield.getPlayerMinoGrounded()) {
            playerDatas[0].lockDelayFrames++;
            if (playerDatas[0].hardDropLock ||
                    lockResetMaxCount <= playerDatas[0].lockResetCount ||
                    lockDelayMaxFrames <= playerDatas[0].lockDelayFrames) {
                return processPieceLock();
            }
        } else {
            playerDatas[0].lockDelayFrames = 0;
        }
        return true;
    }

    /**
     * @return true if gameloop should not end, false if should end
     */
    private boolean processPieceLock() {
        boolean inField = playfield.lockPlayerMino();
        int lines = playfield.clearLines();
        linesCleared += lines;

        if (4 <= lines || (playerDatas[0].lastMoveTSpin && 0 < lines)) {
            b2bCount++;
        } else if (0 < lines) {
            b2bCount = 0;
        }

        if (0 < lines) {
            comboCount++;
        } else {
            comboCount = 0;
        }

        calloutLines = lines;
        if (!zenMode) {
            level = 1 + (linesCleared / 10);
        }
        playerDatas[0].lockResetCount = 0;
        playerDatas[0].lockDelayFrames = 0;
        playerDatas[0].hardDropLock = false;
        renderBlocks = playfield.getRenderBlocks();
        if (playerDatas[0].lastMoveTSpin) {
            spinNameGui = playerDatas[0].spinName;
        }
        playerDatas[0].spinName = null;
        allCleared = playfield.isClear();

        score += calculateLockScore(lines, playerDatas[0].lastMoveTSpin, playerDatas[0].spinMini) * level
                * (1 < b2bCount ? 1.5 : 1);
        score += 50 * (1 < comboCount ? comboCount - 1 : 0) * level;

        windowNudgeY += 4;
        windowNudgeY *= (lines * 0.25) + 1;
        if (!inField) {
            goalState = GoalState.LOSE;
            return false;
        }
        danger = playfield.getDanger();
        return true;
    }

    private int calculateLockScore(int lines, boolean tSpin, boolean spinMini) {
        if (lines < 0) {
            return 0;
        }
        if (!tSpin) {
            switch (lines) {
                case 0:
                    return 0;
                case 1:
                    return 100;
                case 2:
                    return 300;
                case 3:
                    return 500;
                default:
                    return 800;
            }
        }

        if (spinMini) {
            switch (lines) {
                case 0:
                    return 100;
                case 1:
                    return 200;
                default:
                    return 1200;
            }
        }

        switch (lines) {
            case 0:
                return 400;
            case 1:
                return 800;
            case 2:
                return 1200;
            default:
                return 1600;
        }

    }

    /**
     * @return true if gameloop should not end, false if should end
     */
    private boolean processPieceSpawn() {
        boolean spawnSuccess = playfield.spawnPlayerMino(playerDatas[0].getNextMino());
        nextQueueGuiData = playerDatas[0].getNextQueueGuiData();
        if (!spawnSuccess) {
            goalState = GoalState.LOSE;
            return false;
        }
        playerDatas[0].lockHold = false;
        playerDatas[0].gravityCount = 0;
        playerDatas[0].lowestPlayerY = playfield.getPlayerMinoY();
        return true;
    }

    private void renderEnd() {
        danger = false;
        renderFrame();
    }

    private void renderFrame() {
        boolean offerResult = renderQueue.offer(new GuiData(timeMillis,
                linesCleared,
                level,
                score,
                playerDatas[0].hold,
                playerDatas[0].lockHold,
                pdr,
                playerLockProgress,
                windowNudgeX,
                windowNudgeY,
                Math.max(0, comboCount - 1),
                Math.max(0, b2bCount - 1),
                goalState,
                gamemodeName,
                danger,
                renderBlocks,
                nextQueueGuiData,
                calloutLines,
                spinNameGui,
                playerDatas[0].spinMini,
                allCleared,
                goalData,
                countdownGui));
        if (offerResult) {
            renderBlocks = null;
            nextQueueGuiData = null;
            spinNameGui = null;
            calloutLines = 0;
            windowNudgeX = 0;
            windowNudgeY = 0;
            allCleared = false;
            goalData = null;
            countdownGui = -1;
        }
    }

    private void resetLockCount() {
        if (playfield.getPlayerMinoY() < playerDatas[0].lowestPlayerY) {
            playerDatas[0].lockResetCount = 0;
            playerDatas[0].lowestPlayerY = playfield.getPlayerMinoY();
        }
    }

    public GuiData getGuiData() {
        return renderQueue.poll();
    }

    private static double[] levelTable = { 0.016666667, 0.021017234, 0.026977447, 0.035255958,
            0.046921922, 0.063613232, 0.087865741, 0.123701138,
            0.177525297, 0.259807742, 0.387747189, 0.590667454,
            0.918273646, 1.457, 2.361, 3.909, 6.614, 11.438, 20 };

    private static double getGravityFromLevel(int level) {
        return level > 19 ? levelTable[18] : levelTable[level - 1];
    }

    @Override
    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.SonicDrop, x -> {
            sonicDrop = (boolean) x;
        });
        receiversMap.put(SettingKey.DasChargeFrames, x -> {
            pis[0].setDAS((int) x);
        });
        receiversMap.put(SettingKey.AutoRepeatFrames, x -> {
            pis[0].setARR((int) x);
        });
        receiversMap.put(SettingKey.GameplayMode, x -> {
            newGoal.poll();
            switch ((GameplayMode) x) {
                case Marathon:
                    newGoal.offer(new LineGoal(150));
                    gamemodeName = "Marathon";
                    break;
                case Sprint:
                    newGoal.offer(new LineGoal(40));
                    gamemodeName = "Sprint";
                    break;
                case Ultra:
                    newGoal.offer(new TimeGoal(TimeUnit.MINUTES.toMillis(3)));
                    gamemodeName = "Ultra";
                    break;
                case Zen:
                    newGoal.offer(new NoGoal());
                    gamemodeName = "Zen";
                    break;
            }
        });
        return receiversMap;
    }
}
