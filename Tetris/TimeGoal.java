package Tetris;

public class TimeGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private long goalTime;

    public TimeGoal(long goalTime) {
        this.goalTime = goalTime;
    }

    @Override
    protected GoalState calculate(long timeMillis, int lines) {
        if (goalTime < timeMillis) {
            return GoalState.WIN;
        }

        return GoalState.NONE;
    }

    @Override
    public GoalData getGoalData() {
        GoalData gdt = new GoalData();
        gdt.setTimesGoal(goalTime);
        return gdt;
    }
}
