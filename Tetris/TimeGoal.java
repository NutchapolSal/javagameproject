package Tetris;

public class TimeGoal extends Goal {
    private GoalState goalState = GoalState.NONE;
    private int goalTime;

    public TimeGoal(int goalTime) {
        this.goalTime = goalTime;
    }

    @Override
    protected GoalState calculate(int timeMillis, int lines) {
        if (goalTime < timeMillis) {
            goalState = GoalState.WIN;
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
