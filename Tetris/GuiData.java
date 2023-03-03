package Tetris;

public class GuiData {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final Mino hold;
    public final boolean lockHold;
    public final PlayerRenderData playerRenderData;
    public final double windowNudgeX;
    public final double windowNudgeY;
    public final int comboCount;
    public final int b2bCount;
    /**
     * may be null
     */
    public final ObjectDataGrid<MinoColor> renderBlocks;
    /**
     * may be null
     */
    public final Mino[] nextQueue;
    public final int calloutLines;
    /**
     * may be null
     */
    public final String spinText;

    /**
     * @param renderBlocks if null, will keep using old data
     * @param nextQueue    if null, will keep using old data
     * @param calloutLines if not 0, will start animation for line clear callout
     * @param spinText     if not null, will start animation for spin callout
     */
    public GuiData(long timeMillis,
            int linesCleared,
            int level,
            Mino hold,
            boolean lockHold,
            PlayerRenderData playerRenderData,
            double windowNudgeX,
            double windowNudgeY,
            int comboCount,
            int b2bCount,
            ObjectDataGrid<MinoColor> renderBlocks,
            Mino[] nextQueue,
            int calloutLines,
            String spinText) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.nextQueue = nextQueue;
        this.hold = hold;
        this.lockHold = lockHold;
        this.playerRenderData = playerRenderData;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
        this.comboCount = comboCount;
        this.b2bCount = b2bCount;
        this.renderBlocks = renderBlocks;
        this.calloutLines = calloutLines;
        this.spinText = spinText;
    }
}
