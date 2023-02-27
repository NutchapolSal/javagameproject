package Tetris;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

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
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeMillis += 16;
            }
        }, 0, 16);
    }

    class GuiDataSource {
        public final long timeMillis;

        public GuiDataSource() {
            this.timeMillis = Gameplay.this.timeMillis;
        }
    }

    public GuiDataSource getGuiDataSource() {
        return new GuiDataSource();
    }

}
