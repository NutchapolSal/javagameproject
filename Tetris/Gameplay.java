package Tetris;

import java.sql.Time;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.concurrent.ArrayBlockingQueue;

public class Gameplay {
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
                endGameLoop();
                return;
            }

            pi.tick();

            if (pi.getHold() && !lockHold) {
                processHold();
            }

            if (pi.getXMove() != 0) {
                processXMove();
            }

            if (pi.getRotation() != Rotation.None) {
                processRotation();
            }

            if (pi.getHardDrop()) {
                processHardDrop();
            }
            if (pi.getSoftDrop()) {
                processSoftDrop();
            }

            processGravity();

            processLockDelay();

            playerLockProgress = (double) lockDelayFrames / lockDelayMaxFrames;
            pdr = playfield.getPlayerRenderData();

            if (!playfield.hasPlayerMino()) {
                processPieceSpawn();
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

    private static long FRAME_DELAY = 16_666_666;
    private int lockResetMaxCount = 15;
    private int lockDelayMaxFrames = 30;
    private boolean sonicDrop = false;

    private PlayerInput pi = new PlayerInput();
    private Timer timer = new Timer();
    private TimerTask gameLoop;
    private TimerTask countdownTask;
    private Goal goal;
    private Queue<Goal> newGoal = new ArrayBlockingQueue<>(1);
    private long startTime;
    private long lastFrame;
    private long timeMillis;
    private Playfield playfield;
    private MinoRandomizer minoRandomizer;
    private Queue<Mino> nextQueue = new ArrayDeque<>();
    private Mino hold;
    private boolean lockHold;
    private int linesCleared;
    private int level;
    private int b2bCount;
    private int comboCount;
    private boolean lastMoveTSpin;
    private double gravityCount = 0;
    private int lockDelayFrames = 0;
    private int lockResetCount = 0;
    private int lowestPlayerY;
    private boolean hardDropLock;
    private String spinName;
    private boolean spinMini;
    private int countdown;

    private boolean zenMode;

    private Queue<GuiData> renderQueue = new ArrayBlockingQueue<>(3);
    private PlayerRenderData pdr;
    private double playerLockProgress;
    private double windowNudgeX;
    private double windowNudgeY;
    private ObjectDataGrid<MinoColor> renderBlocks;
    private Mino[] nextQueueGuiData = new Mino[6];
    private int calloutLines;
    private String spinNameGui;
    private boolean allCleared;
    private GoalData goalData;
    private GoalState goalState;
    private int countdownGui;
    private String gamemodeName = "";

    public void setRawInputSource(RawInputSource ris) {
        pi.setRawInputSource(ris);
    }

    public void startGame() {
        if (gameLoop != null) {
            gameLoop.cancel();
        }

        gameLoop = new GameLoopTask();
        countdownTask = new CountdownTask();
        goal = newGoal.peek() != null ? newGoal.poll() : goal;
        lastFrame = System.nanoTime();
        timeMillis = 0;
        playfield = new Playfield();
        minoRandomizer = new SevenBagRandomizer(System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(5));
        nextQueue = new ArrayDeque<>();
        hold = null;
        lockHold = false;
        linesCleared = 0;
        level = 1;
        b2bCount = 0;
        comboCount = 0;
        lastMoveTSpin = false;
        gravityCount = 0;
        lockDelayFrames = 0;
        lockResetCount = 0;
        lowestPlayerY = playfield.getPlayerMinoY();
        hardDropLock = false;
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
        spinName = null;
        spinMini = false;
        allCleared = false;
        goalData = goal.getGoalData();

        fillNextQueue();
        playfield.spawnPlayerMino(getNextMino());
        renderFrame();

        timer.scheduleAtFixedRate(countdownTask, 0, 750);
    }

    private void startGameLoop() {
        startTime = System.nanoTime();
        lastFrame = System.nanoTime();
        timer.scheduleAtFixedRate(gameLoop, 0, 3);
    }

    private void processHold() {
        if (hold == null) {
            hold = playfield.swapHold(getNextMino());
        } else {
            hold = playfield.swapHold(hold);
        }
        lockHold = true;
        lockResetCount = 0;
        lockDelayFrames = 0;
        gravityCount = 0;
        lowestPlayerY = playfield.getPlayerMinoY();
    }

    private void processXMove() {
        if (playfield.moveXPlayerMino(pi.getXMove())) {
            resetLockDelay();
            lastMoveTSpin = false;
        } else {
            windowNudgeX += pi.getXMove() * 3;
        }
    }

    private void processRotation() {
        lastMoveTSpin = false;
        spinMini = false;
        switch (playfield.rotatePlayerMino(pi.getRotation())) {
            case SuccessTSpinMini:
                spinMini = true;
                // fallthrough
            case SuccessTSpin:
                lastMoveTSpin = true;
                // fallthrough
            case SuccessTwist:
                spinName = playfield.getPlayerMinoName();
                windowNudgeX += calculateRotationNudge();
                // fallthrough
            case Success:
                resetLockDelay();
                // fallthrough
            case Fail:
                break;
        }
    }

    private int calculateRotationNudge() {
        switch (pi.getRotation()) {
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
        boolean moved = playfield.sonicDropPlayerMino();
        if (moved) {
            lastMoveTSpin = false;
        }
        hardDropLock = true;
        windowNudgeY += 6;
    }

    private void processSoftDrop() {
        if (sonicDrop) {
            boolean moved = playfield.sonicDropPlayerMino();
            if (moved) {
                resetLockCount();
            }
        } else {
            gravityCount += getGravityFromLevel(level) * 6;
        }
    }

    private void processGravity() {
        gravityCount += getGravityFromLevel(level);
        int dropCount = (int) gravityCount;
        for (int i = 0; i < dropCount; i++) {
            if (playfield.moveYPlayerMino(-1)) {
                resetLockCount();
                lastMoveTSpin = false;
            } else {
                break;
            }
        }
        gravityCount -= dropCount;
    }

    private void processLockDelay() {
        if (playfield.getPlayerMinoGrounded()) {
            lockDelayFrames++;
            if (hardDropLock ||
                    lockResetMaxCount <= lockResetCount ||
                    lockDelayMaxFrames <= lockDelayFrames) {
                processPieceLock();
            }
        } else {
            lockDelayFrames = 0;
        }
    }

    private void processPieceLock() {
        boolean inField = playfield.lockPlayerMino();
        int lines = playfield.clearLines();
        linesCleared += lines;

        if (4 <= lines || (lastMoveTSpin && 0 < lines)) {
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
        lockResetCount = 0;
        lockDelayFrames = 0;
        hardDropLock = false;
        renderBlocks = playfield.getRenderBlocks();
        if (lastMoveTSpin) {
            spinNameGui = spinName;
        }
        spinName = null;
        allCleared = playfield.isClear();

        windowNudgeY += 4;
        windowNudgeY *= (lines * 0.25) + 1;
        if (!inField) {
            goalState = GoalState.LOSE;
            endGameLoop();
        }
    }

    private void processPieceSpawn() {
        boolean spawnSuccess = playfield.spawnPlayerMino(getNextMino());
        if (!spawnSuccess) {
            goalState = GoalState.LOSE;
            endGameLoop();
        }
        lockHold = false;
        gravityCount = 0;
        lowestPlayerY = playfield.getPlayerMinoY();
    }

    private void endGameLoop() {
        gameLoop.cancel();
        renderFrame();
    }

    private void renderFrame() {
        boolean offerResult = renderQueue.offer(new GuiData(timeMillis,
                linesCleared,
                level,
                hold,
                lockHold,
                pdr,
                playerLockProgress,
                windowNudgeX,
                windowNudgeY,
                Math.max(0, comboCount - 1),
                Math.max(0, b2bCount - 1),
                goalState,
                gamemodeName,
                renderBlocks,
                nextQueueGuiData,
                calloutLines,
                spinNameGui,
                spinMini,
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

    private Mino getNextMino() {
        nextQueue.offer(minoRandomizer.next());
        var nextMino = nextQueue.poll();
        nextQueueGuiData = nextQueue.toArray(new Mino[0]);
        return nextMino;
    }

    private void fillNextQueue() {
        for (int i = 0; i < 5; i++) {
            nextQueue.offer(minoRandomizer.next());
        }
        nextQueueGuiData = nextQueue.toArray(new Mino[0]);
    }

    private void resetLockDelay() {
        lockDelayFrames = 0;
        lockResetCount++;
    }

    private void resetLockCount() {
        if (playfield.getPlayerMinoY() < lowestPlayerY) {
            lockResetCount = 0;
            lowestPlayerY = playfield.getPlayerMinoY();
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

    /**
     * @return {@code Consumer<Object>} but the {@code Object} is casted to
     *         {@code boolean} inside
     */
    public Consumer<Object> getSonicDropReceiver() {
        return x -> {
            sonicDrop = (boolean) x;
        };
    }

    /**
     * @return {@code Consumer<Object>} but the {@code Object} is casted to
     *         {@code int} inside
     */
    public Consumer<Object> getDASReceiver() {
        return x -> {
            pi.setDAS((int) x);
        };
    }

    /**
     * @return {@code Consumer<Object>} but the {@code Object} is casted to
     *         {@code int} inside
     */
    public Consumer<Object> getARRReceiver() {
        return x -> {
            pi.setARR((int) x);
        };
    }

    /**
     * @return {@code Consumer<Object>} but the {@code Object} is casted to
     *         {@code GameplayMode} inside
     */
    public Consumer<Object> getGameplayModeReceiver() {
        return x -> {
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
        };
    }
}
