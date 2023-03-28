package Tetris.gameplay.goal;

import Tetris.data.GoalData;

public class LineGoal extends Goal {
    private int goalLines;

    public LineGoal(int goalLines) {
        this.goalLines = goalLines;
    }

    @Override
    public GoalState calculate(long timeMillis, int lines) {
        if (lines >= goalLines) {
            return GoalState.WIN;
        }

        return GoalState.NONE;
    }

    @Override
    public GoalData getGoalData() {
        GoalData gdt = new GoalData();
        gdt.setLinesGoal(goalLines);
        return gdt;
    }
}
