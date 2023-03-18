package Tetris;

public class TimeGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private long goalTime;
    private int lines;
    private long timeMillis;

    public TimeGoal(long goalTime) {
        this.goalTime = goalTime;
    }

    @Override
    protected GoalState calculate(long timeMillis, int lines) {
        this.timeMillis = timeMillis;
        this.lines = lines;

        if (goalTime < timeMillis) {
            goalState = GoalState.WIN;
        }

        return goalState;
    }

    @Override
    public GoalData getGoalData() {
        GoalData gdt = new GoalData();
        gdt.setTimesGoal(goalTime);
        return gdt;
    }
}
