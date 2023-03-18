package Tetris;

public class LineGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private int goalLines;
    private int lines;
    private long timeMillis;

    public LineGoal(int goalLines) {
        this.goalLines = goalLines;
    }

    @Override
    protected GoalState calculate(long timeMillis, int lines) {
        this.timeMillis = timeMillis;
        this.lines = lines;

        if (lines >= goalLines) {
            goalState = GoalState.WIN;
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
