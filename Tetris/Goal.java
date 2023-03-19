package Tetris;

public abstract class Goal {
    protected abstract GoalState calculate(long timeMillis, int lines);

    public abstract GoalData getGoalData();
}
