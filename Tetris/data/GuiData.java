package Tetris.data;

import Tetris.data.mino.Mino;
import Tetris.gameplay.goal.GoalState;

public class GuiData {
    public final long timeMillis;
    public final int linesCleared;
    public final int level;
    public final int score;
    public final PlayerGuiData[] playerGuiDatas;
    public final double windowNudgeX;
    public final double windowNudgeY;
    public final int comboCount;
    public final int b2bCount;
    public final GoalState goalState;
    public final String gamemodeName;
    public final boolean danger;
    /**
     * may be null
     */
    public final ObjectDataGrid<BlockWithConnection> renderBlocks;
    public final int calloutLines;
    /**
     * may be null
     */
    public final String spinName;
    public final boolean spinMini;
    public final boolean allClear;
    public final GoalData goalData;
    public final int countdown;

    /**
     * @param renderBlocks if null, will keep using old data
     * @param calloutLines if not 0, will start animation for line clear callout
     * @param spinName     if not null, will start animation for spin callout
     * @param allClear     if true, will start animation for all clear callout
     * @param goalData     if null, will keep using old data
     * @param countdown    if not -1, will start animation for that value
     */
    public GuiData(long timeMillis,
            int linesCleared,
            int level,
            int score,
            PlayerGuiData[] playerGuiDatas,
            double windowNudgeX,
            double windowNudgeY,
            int comboCount,
            int b2bCount,
            GoalState goalState,
            String gamemodeName,
            boolean danger,
            ObjectDataGrid<BlockWithConnection> renderBlocks,
            int calloutLines,
            String spinName,
            boolean spinMini,
            boolean allClear,
            GoalData goalData,
            int countdown) {
        this.timeMillis = timeMillis;
        this.linesCleared = linesCleared;
        this.level = level;
        this.score = score;
        this.playerGuiDatas = playerGuiDatas;
        this.windowNudgeX = windowNudgeX;
        this.windowNudgeY = windowNudgeY;
        this.comboCount = comboCount;
        this.b2bCount = b2bCount;
        this.goalState = goalState;
        this.gamemodeName = gamemodeName;
        this.danger = danger;
        this.renderBlocks = renderBlocks;
        this.calloutLines = calloutLines;
        this.spinName = spinName;
        this.spinMini = spinMini;
        this.allClear = allClear;
        this.goalData = goalData;
        this.countdown = countdown;
    }
}
