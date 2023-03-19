package Tetris;

public class LineGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private int goalLines;

    public LineGoal(int goalLines) {
        this.goalLines = goalLines;
    }

    @Override
    protected GoalState calculate(long timeMillis, int lines) {
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
