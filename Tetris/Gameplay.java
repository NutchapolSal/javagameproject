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
    private Queue<Mino> nextQueue = new ArrayDeque<>();
    private Mino hold;
    private ObjectDataGrid<MinoColor> playfield;
    private int linesCleared;
    private int level;
    private Mino playerMino;
    private long lastFrame;
    private MinoRandomizer minoRandomizer;
    private boolean lockHold;
    private static long FRAME_DELAY = 16_666_666;
    // private static long FRAME_DELAY = 250_000_000;

    private long timeMillis;

    private GuiDataSource gds = new GuiDataSource();

    public GuiDataSource getGds() {
        return gds;
    }

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
        playfield = new ObjectDataGrid<>(10, 40);
        linesCleared = 0;
        level = 1;
        minoRandomizer = new SevenBagRandomizer(1234); //TODO: actually use random seed

        fillNextQueue();
        playerMino = getNextMino();

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

                pi.tick();

                int xmove = pi.getXMove();

                if (xmove == 1) {
                    getNextMino();
                }

                if (xmove == -1) {
                    hold = getNextMino();
                }

            }
        }, 0, 1);
    }

    private Mino getNextMino() {
        nextQueue.offer(minoRandomizer.next());
        return nextQueue.poll();
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

        public GuiDataSource() {
            this.timeMillis = Gameplay.this.timeMillis;
            this.linesCleared = Gameplay.this.linesCleared;
            this.level = Gameplay.this.level;
            this.nextQueue = Gameplay.this.nextQueue.toArray(new Mino[5]);
            this.hold = Gameplay.this.hold;
            // this.lockHold = Gameplay.this.lockHold;
            this.lockHold = true;
        }
    }

    public GuiDataSource getGuiDataSource() {
        return new GuiDataSource();
    }

}
