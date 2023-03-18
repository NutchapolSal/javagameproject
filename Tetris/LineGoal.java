package Tetris;

public class LineGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private int goalLines;
    private int lines;
    private int timeMillis;

    public LineGoal(int goalLines) {
        this.goalLines = goalLines;
    }

    @Override
    protected GoalState calculate(int timeMillis, int lines) {
        this.timeMillis = timeMillis;
        this.lines = lines;

        if (lines == goalLines && timeMillis > 0) {
            goalState = GoalState.WIN;
        } else if (timeMillis <= 0) {
            goalState = GoalState.LOSE;
        }

        return goalState;
    }

    @Override
    public GoalData getGoalData() {
        GoalData gdt = new GoalData();
        gdt.setLinesGoal(goalLines);
        return gdt;
    }
}
