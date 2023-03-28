package Tetris.gameplay.goal;

import Tetris.GoalData;

public abstract class Goal {
    public abstract GoalState calculate(long timeMillis, int lines);

    public abstract GoalData getGoalData();
}
