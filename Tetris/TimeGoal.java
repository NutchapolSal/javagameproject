package Tetris;

public class TimeGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private int goalTime;
    private int lines;
    private int timeMillis;

    public TimeGoal(int goalTime) {
        this.goalTime = goalTime;
    }

    @Override
    protected GoalState calculate(int timeMillis, int lines) {
        this.timeMillis = timeMillis;
        this.lines = lines;

        if (timeMillis < goalTime) {
            goalState = GoalState.WIN;
        } else if (timeMillis <= 0) {
            goalState = GoalState.LOSE;
        }

        return goalState;
    }

    @Override
    public GoalData getGoalData() {
        GoalData gdt = new GoalData();
        gdt.setLinesGoal(goalTime);
        return gdt;
    }
}
