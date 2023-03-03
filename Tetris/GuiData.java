package Tetris;

public class GuiData {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final Mino[] nextQueue;
    public final Mino hold;
    public final boolean lockHold;
    public final ObjectDataGrid<MinoColor> renderBlocks;
    public final double windowNudgeX;
    public final double windowNudgeY;
    public final PlayerRenderData playerRenderData;

    public GuiData(long timeMillis, int linesCleared, int level, Mino[] nextQueue, Mino hold, boolean lockHold,
            ObjectDataGrid<MinoColor> renderBlocks, double windowNudgeX, double windowNudgeY,
            PlayerRenderData playerRenderData) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.nextQueue = nextQueue;
        this.hold = hold;
        this.lockHold = lockHold;
        this.renderBlocks = renderBlocks;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
        this.playerRenderData = playerRenderData;
    }
}
