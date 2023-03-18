package Tetris;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class Gameplay {
    private final class GameLoopTask extends TimerTask {
        public void run() {
            long nowFrame = System.nanoTime();
            if (nowFrame - lastFrame < FRAME_DELAY) {
                return;
            }
            lastFrame += FRAME_DELAY;
            timeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - System.nanoTime());
            if (timeMillis <= 0) {
                timeMillis = 0;
                this.cancel();
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

    private static long FRAME_DELAY = 16_666_666;
    private int lockResetMaxCount = 15;
    private int lockDelayMaxFrames = 30;

    private PlayerInput pi = new PlayerInput();
    private Timer timer = new Timer();
    private TimerTask gameLoop;
    private long endTime;
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

    private Queue<GuiData> renderQueue = new ArrayBlockingQueue<>(3);
    private PlayerRenderData pdr;
    private double playerLockProgress;
    private double windowNudgeX;
    private double windowNudgeY;
    private ObjectDataGrid<MinoColor> renderBlocks;
    private Mino[] nextQueueGuiData = new Mino[6];
    private int calloutLines;
    private String spinNameGui;

    public void setRawInputSource(RawInputSource ris) {
        pi.setRawInputSource(ris);
    }

    public void startGame() {
        if (gameLoop != null) {
            gameLoop.cancel();
        }

        gameLoop = new GameLoopTask();
        lastFrame = System.nanoTime();
        timeMillis = 0;
        playfield = new Playfield();
        minoRandomizer = new SevenBagRandomizer(1234); // TODO: actually use random seed
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

        pdr = playfield.getPlayerRenderData();
        playerLockProgress = 0;
        windowNudgeX = 0;
        windowNudgeY = 0;
        renderBlocks = playfield.getRenderBlocks();
        calloutLines = 0;
        spinName = null;
        spinMini = false;

        fillNextQueue();
        playfield.spawnPlayerMino(getNextMino());
        renderFrame();

        endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(2) + TimeUnit.SECONDS.toNanos(0);

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
        gravityCount += getGravityFromLevel(level) * 6;
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
        playfield.lockPlayerMino();
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
        level = 1 + (linesCleared / 10);
        lockResetCount = 0;
        lockDelayFrames = 0;
        hardDropLock = false;
        renderBlocks = playfield.getRenderBlocks();
        if (lastMoveTSpin) {
            spinNameGui = spinName;
        }
        spinName = null;

        windowNudgeY += 4;
        windowNudgeY *= (lines * 0.25) + 1;
    }

    private void processPieceSpawn() {
        boolean spawnSuccess = playfield.spawnPlayerMino(getNextMino());
        if (!spawnSuccess) {
            timer.cancel();
        }
        lockHold = false;
        gravityCount = 0;
        lowestPlayerY = playfield.getPlayerMinoY();
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
                renderBlocks,
                nextQueueGuiData,
                calloutLines,
                spinNameGui,
                spinMini));
        if (offerResult) {
            renderBlocks = null;
            nextQueueGuiData = null;
            spinNameGui = null;
            calloutLines = 0;
            windowNudgeX = 0;
            windowNudgeY = 0;
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
}
