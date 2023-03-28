package Tetris.gameplay.goal;

import Tetris.data.GoalData;

public class NoGoal extends Goal {

    @Override
    public GoalState calculate(long timeMillis, int lines) {
        return GoalState.NONE;
    }

    @Override
    public GoalData getGoalData() {
        return new GoalData();
    }
}
