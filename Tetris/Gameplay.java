package Tetris;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gameplay {
    private static long FRAME_DELAY = 16_666_666;
    // private static long FRAME_DELAY = 250_000_000;
    private int lockResetMaxCount = 15;
    private int lockDelayMaxFrames = 30;
    private int gravityMaxFrames = 256;

    private Queue<Mino> nextQueue = new ArrayDeque<>();
    private Mino hold;
    private Playfield playfield;
    private int linesCleared;
    private int level;
    private long lastFrame;
    private MinoRandomizer minoRandomizer;
    private boolean lockHold;
    private ObjectDataGrid<MinoColor> renderBlocks;
    private double windowNudgeX;
    private double windowNudgeY;

    private Mino[] nextQueueGuiData = new Mino[6];

    private int gravityFrames = 0;
    private int lockDelayFrames = 0;
    private int lockResetCount = 0;
    private int lowestPlayerY;

    private long timeMillis;

    public long getTimeMillis() {
        return timeMillis;
    }

    private PlayerInput pi = new PlayerInput();

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

        Timer timer = new Timer();
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
                    if (playfield.rotatePlayerMino(pi.getRotation())) {
                        resetLockDelay();
                    }
                }

                boolean hardDropLock = false;
                if (pi.getHardDrop()) {
                    playfield.sonicDropPlayerMino();
                    hardDropLock = true;
                    windowNudgeY += 6;
                }
                if (pi.getSoftDrop()) {
                    gravityFrames += 30;
                }

                gravityFrames++;
                if (gravityMaxFrames < gravityFrames) {
                    gravityFrames -= gravityMaxFrames;
                    if (playfield.moveYPlayerMino(-1)) {
                        resetLockCount();
                    }
                }

                if (playfield.getPlayerMinoGrounded()) {
                    lockDelayFrames++;
                    if (hardDropLock || lockResetMaxCount <= lockResetCount || lockDelayMaxFrames <= lockDelayFrames) {
                        windowNudgeY += 4;
                        playfield.lockPlayerMino();
                        lockResetCount = 0;
                        lockDelayFrames = 0;
                    }
                } else {
                    lockDelayFrames = 0;
                }

                renderBlocks = playfield.getRenderBlocks();
                level = lockDelayFrames;
                linesCleared = lockResetCount;

                if (!playfield.hasPlayerMino()) {
                    boolean spawnSuccess = playfield.spawnPlayerMino(getNextMino());
                    if (!spawnSuccess) {
                        timer.cancel();
                    }
                    lockHold = false;
                    gravityFrames = 0;
                    lowestPlayerY = playfield.getPlayerMinoY();
                }
            }
        }, 0, 3);
    }

    private Mino getNextMino() {
        nextQueue.offer(minoRandomizer.next());
        var nextMino = nextQueue.poll();
        nextQueueGuiData = nextQueue.toArray(nextQueueGuiData);
        return nextMino;
        // return Tetromino.L();
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
        var gds = new GuiData(timeMillis,
                linesCleared,
                level,
                nextQueueGuiData,
                hold,
                lockHold,
                renderBlocks,
                windowNudgeX,
                windowNudgeY);
        windowNudgeX = 0;
        windowNudgeY = 0;
        return gds;
    }
}
