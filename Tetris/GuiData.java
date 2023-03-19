package Tetris;

public class GuiData {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final Mino hold;
    public final boolean lockHold;
    public final PlayerRenderData playerRenderData;
    public final double playerLockProgress;
    public final double windowNudgeX;
    public final double windowNudgeY;
    public final int comboCount;
    public final int b2bCount;
    public final GoalState goalState;
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
    public final String spinName;
    public final boolean spinMini;
    public final boolean allClear;
    public final GoalData goalData;

    /**
     * @param renderBlocks if null, will keep using old data
     * @param nextQueue    if null, will keep using old data
     * @param calloutLines if not 0, will start animation for line clear callout
     * @param spinName     if not null, will start animation for spin callout
     * @param allClear     if true, will start animation for all clear callout
     * @param goalData     if null, will keep using old data
     */
    public GuiData(long timeMillis,
            int linesCleared,
            int level,
            Mino hold,
            boolean lockHold,
            PlayerRenderData playerRenderData,
            double playerLockProgress,
            double windowNudgeX,
            double windowNudgeY,
            int comboCount,
            int b2bCount,
            GoalState goalState,
            ObjectDataGrid<MinoColor> renderBlocks,
            Mino[] nextQueue,
            int calloutLines,
            String spinName,
            boolean spinMini,
            boolean allClear,
            GoalData goalData) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.nextQueue = nextQueue;
        this.hold = hold;
        this.lockHold = lockHold;
        this.playerRenderData = playerRenderData;
        this.playerLockProgress = playerLockProgress;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
        this.comboCount = comboCount;
        this.b2bCount = b2bCount;
        this.goalState = goalState;
        this.renderBlocks = renderBlocks;
        this.calloutLines = calloutLines;
        this.spinName = spinName;
        this.spinMini = spinMini;
        this.allClear = allClear;
        this.goalData = goalData;
    }
}
