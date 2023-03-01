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

    private int gravityFrames = 0;
    private int lockDelayFrames = 0;
    private int lockResetCount = 0;

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
                gravityFrames++;

                pi.tick();

                if (!playfield.moveXPlayerMino(pi.getXMove())) {
                    windowNudgeX += pi.getXMove() * 3;
                }
                if (pi.getRotation() != Rotation.None) {
                    playfield.rotatePlayerMino(pi.getRotation());
                }
                if (pi.getHardDrop()) {
                    playfield.sonicDropPlayerMino();
                    lockDelayFrames = lockDelayMaxFrames;
                    windowNudgeY += 10;
                }
                if (pi.getSoftDrop()) {
                    gravityFrames += 30;
                }
                if (pi.getHold() && !lockHold) {
                    if (hold == null) {
                        hold = playfield.swapHold(getNextMino());
                    } else {
                        hold = playfield.swapHold(hold);
                    }
                    lockHold = true;
                }

                if (playfield.getPlayerMinoGrounded()) {
                    lockDelayFrames++;
                    if (lockDelayMaxFrames <= lockDelayFrames) {
                        playfield.lockPlayerMino();
                        windowNudgeY += 4;
                    }
                } else {
                    lockDelayFrames = 0;
                }

                renderBlocks = playfield.getRenderBlocks();

                if (16 < gravityFrames) {
                    playfield.moveYPlayerMino(-1);
                    gravityFrames = 0;
                }

                if (!playfield.hasPlayerMino()) {
                    lockHold = false;
                    gravityFrames = 0;
                    boolean spawnSuccess = playfield.spawnPlayerMino(getNextMino());
                    if (!spawnSuccess) {
                        timer.cancel();
                    }
                }
            }
        }, 0, 3);
    }

    private Mino getNextMino() {
        nextQueue.offer(minoRandomizer.next());
        return nextQueue.poll();
        // return Tetromino.L();
    }

    private void fillNextQueue() {
        for (int i = 0; i < 5; i++) {
            nextQueue.offer(minoRandomizer.next());
        }
    }

    class GuiDataSource {
        public final long timeMillis;
        public final int linesCleared;
        public final int level;
        public final Mino[] nextQueue;
        public final Mino hold;
        public final boolean lockHold;
        public final ObjectDataGrid<MinoColor> renderBlocks;
        public final double windowNudgeX;
        public final double windowNudgeY;

        public GuiDataSource() {
            this.timeMillis = Gameplay.this.timeMillis;
            this.linesCleared = Gameplay.this.linesCleared;
            this.level = Gameplay.this.level;
            this.nextQueue = Gameplay.this.nextQueue.toArray(new Mino[5]);
            this.hold = Gameplay.this.hold;
            this.lockHold = Gameplay.this.lockHold;
            this.renderBlocks = Gameplay.this.renderBlocks;
            this.windowNudgeX = Gameplay.this.windowNudgeX;
            this.windowNudgeY = Gameplay.this.windowNudgeY;
        }
    }

    public GuiDataSource getGuiDataSource() {
        var gds = new GuiDataSource();
        windowNudgeX = 0;
        windowNudgeY = 0;
        return gds;
    }
}
