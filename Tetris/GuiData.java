package Tetris;

public class GuiData {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final Mino[] nextQueue;
    public final Mino hold;
    public final boolean lockHold;
    public final PlayerRenderData playerRenderData;
    public final double windowNudgeX;
    public final double windowNudgeY;
    public final ObjectDataGrid<MinoColor> renderBlocks;
    public final int calloutLines;
    public final String spinText;
    public final int comboCount;
    public final int b2bCount;

    public GuiData(long timeMillis,
            int linesCleared,
            int level,
            Mino[] nextQueue,
            Mino hold,
            boolean lockHold,
            PlayerRenderData playerRenderData,
            double windowNudgeX,
            double windowNudgeY,
            ObjectDataGrid<MinoColor> renderBlocks,
            int calloutLines,
            String spinText,
            int comboCount,
            int b2bCount) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.nextQueue = nextQueue;
        this.hold = hold;
        this.lockHold = lockHold;
        this.playerRenderData = playerRenderData;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
        this.renderBlocks = renderBlocks;
        this.calloutLines = calloutLines;
        this.spinText = spinText;
        this.comboCount = comboCount;
        this.b2bCount = b2bCount;
    }
}
