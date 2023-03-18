package Tetris;

public abstract class Goal {
    protected abstract GoalState calculate(int timeMillis, int lines);

    public abstract GoalData getGoalData();
}
