package Tetris;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class Gameplay {
    private static long FRAME_DELAY = 16_666_666;
    private int lockResetMaxCount = 15;
    private int lockDelayMaxFrames = 30;

    private Queue<Mino> nextQueue = new ArrayDeque<>();
    private Mino hold;
    private Playfield playfield;
    private int linesCleared;
    private int level;
    private long lastFrame;
    private MinoRandomizer minoRandomizer;
    private boolean lockHold;
    private ObjectDataGrid<MinoColor> renderBlocks;
    private PlayerRenderData pdr;
    private double windowNudgeX;
    private double windowNudgeY;
    private Timer timer;
    private Queue<GuiData> renderQueue = new ArrayBlockingQueue<>(3);

    private Mino[] nextQueueGuiData = new Mino[6];

    private double gravityCount = 0;
    private int lockDelayFrames = 0;
    private int lockResetCount = 0;
    private int lowestPlayerY;

    private long timeMillis;

    public long getTimeMillis() {
        return timeMillis;
    }

    private PlayerInput pi = new PlayerInput();
    private int calloutLines;
    private String spinName;
    private boolean spinMini;
    private double playerLockProgress;

    public void setRawInputSource(RawInputSource ris) {
        pi.setRawInputSource(ris);
    }

    public void startGame() {
        nextQueue = new ArrayDeque<>();
        hold = null;
        playfield = new Playfield();
        linesCleared = 0;
        level = 1;
        minoRandomizer = new SevenBagRandomizer(1234); // TODO: actually use random seed
        lockHold = false;

        fillNextQueue();
        playfield.spawnPlayerMino(getNextMino());
        lowestPlayerY = playfield.getPlayerMinoY();
        renderBlocks = playfield.getRenderBlocks();
        renderFrame();

        if (timer != null)
            timer.cancel();
        timer = new Timer();
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(2) + TimeUnit.SECONDS.toNanos(0);
        lastFrame = System.nanoTime();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long nowFrame = System.nanoTime();
                if (nowFrame - lastFrame < FRAME_DELAY) {
                    return;
                }
                lastFrame += FRAME_DELAY;
                timeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - System.nanoTime());
                if (timeMillis <= 0) {
                    timeMillis = 0;
                    timer.cancel();
                }

                renderBlocks = null;
                nextQueueGuiData = null;
                spinName = null;
                calloutLines = 0;
                windowNudgeX = 0;
                windowNudgeY = 0;

                pi.tick();

                if (pi.getHold() && !lockHold) {
                    if (hold == null) {
                        hold = playfield.swapHold(getNextMino());
                    } else {
                        hold = playfield.swapHold(hold);
                    }
                    lockHold = true;
                }

                boolean nudgePlayfieldX = false;
                if (pi.getXMove() != 0) {
                    boolean moveSuccess = playfield.moveXPlayerMino(pi.getXMove());
                    nudgePlayfieldX = !moveSuccess;
                    if (moveSuccess) {
                        resetLockDelay();
                    }
                }

                if (nudgePlayfieldX) {
                    windowNudgeX += pi.getXMove() * 3;
                }

                if (pi.getRotation() != Rotation.None) {
                    RotationResult result = playfield.rotatePlayerMino(pi.getRotation());
                    spinMini = false;
                    switch (result) {
                        case SuccessTSpinMini:
                            spinMini = true;
                            // fallthrough
                        case SuccessTSpin:
                        case SuccessTwist:
                            spinName = playfield.getPlayerMinoName();
                            // fallthrough
                        case Success:
                            resetLockDelay();
                            // fallthrough
                        case Fail:
                            break;
                    }
                }

                boolean hardDropLock = false;
                if (pi.getHardDrop()) {
                    playfield.sonicDropPlayerMino();
                    hardDropLock = true;
                    windowNudgeY += 6;
                }
                if (pi.getSoftDrop()) {
                    gravityCount += getGravityFromLevel(level) * 6;
                }

                gravityCount += getGravityFromLevel(level);
                int dropCount = (int) gravityCount;
                for (int i = 0; i < dropCount; i++) {
                    if (playfield.moveYPlayerMino(-1)) {
                        resetLockCount();
                    }
                    gravityCount -= 1;
                }

                if (playfield.getPlayerMinoGrounded()) {
                    lockDelayFrames++;
                    if (hardDropLock || lockResetMaxCount <= lockResetCount || lockDelayMaxFrames <= lockDelayFrames) {
                        windowNudgeY += 4;
                        playfield.lockPlayerMino();
                        int lines = playfield.clearLines();
                        linesCleared += lines;
                        calloutLines = lines;
                        level = 1 + (linesCleared / 10);
                        lockResetCount = 0;
                        lockDelayFrames = 0;
                        renderBlocks = playfield.getRenderBlocks();
                    }
                } else {
                    lockDelayFrames = 0;
                }

                renderBlocks = playfield.getRenderBlocks();
                pdr = playfield.getPlayerRenderData();
                playerLockProgress = (double) lockDelayFrames / lockDelayMaxFrames;

                if (!playfield.hasPlayerMino()) {
                    boolean spawnSuccess = playfield.spawnPlayerMino(getNextMino());
                    if (!spawnSuccess) {
                        timer.cancel();
                    }
                    lockHold = false;
                    gravityCount = 0;
                    lowestPlayerY = playfield.getPlayerMinoY();
                }

                renderFrame();
            }
        }, 0, 3);

    }

    private void renderFrame() {
        renderQueue.offer(new GuiData(timeMillis,
                linesCleared,
                level,
                hold,
                lockHold,
                pdr,
                playerLockProgress,
                windowNudgeX,
                windowNudgeY,
                0,
                0,
                renderBlocks,
                nextQueueGuiData,
                calloutLines,
                spinName,
                spinMini));
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
