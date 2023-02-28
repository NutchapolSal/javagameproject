package Tetris;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Gameplay {
    private Queue<Mino> nextQueue;
    private Mino hold;
    private ObjectDataGrid<MinoColor> playfield;
    private int linesCleared;
    private int level;

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

        Timer timer = new Timer();
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(0) + TimeUnit.SECONDS.toNanos(2);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - System.nanoTime());
                if (timeMillis <= 0) {
                    timeMillis = 0;
                    timer.cancel();
                }
            }
        }, 0, 16);
    }

    class GuiDataSource {
        public final long timeMillis;
        public final int linesCleared;
        public final int level;

        public GuiDataSource() {
            this.timeMillis = Gameplay.this.timeMillis;
            this.linesCleared = Gameplay.this.linesCleared;
            this.level = Gameplay.this.level;
        }
    }

    public GuiDataSource getGuiDataSource() {
        return new GuiDataSource();
    }

}
