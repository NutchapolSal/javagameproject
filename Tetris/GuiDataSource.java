package Tetris;

public class GuiDataSource {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final Mino[] nextQueue;
    public final Mino hold;
    public final boolean lockHold;
    public final ObjectDataGrid<MinoColor> renderBlocks;
    public final double windowNudgeX;
    public final double windowNudgeY;

    public GuiDataSource(long timeMillis, int linesCleared, int level, Mino[] nextQueue, Mino hold, boolean lockHold,
            ObjectDataGrid<MinoColor> renderBlocks, double windowNudgeX, double windowNudgeY) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.nextQueue = nextQueue;
        this.hold = hold;
        this.lockHold = lockHold;
        this.renderBlocks = renderBlocks;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
    }
}
