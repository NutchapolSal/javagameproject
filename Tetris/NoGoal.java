package Tetris;

public class NoGoal extends Goal {

    @Override
    protected GoalState calculate(int timeMillis, int lines) {
        return GoalState.NONE;
    }

    @Override
    public GoalData getGoalData() {
        return new GoalData();
    }
}
