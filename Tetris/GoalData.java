package Tetris;

public class GoalData {

    private boolean linesGoal = false;
    private boolean timesGoal = false;
    private int linesCount;
    private long timeMillisLength;

    public GoalData() {
    }

    public void setLinesGoal(int linesCount) {
        this.linesCount = linesCount;
        this.linesGoal = true;
    }

    public void setTimesGoal(long timeMillisLength) {
        this.timeMillisLength = timeMillisLength;
        this.timesGoal = true;
    }

    public boolean isLinesGoal() {
        return linesGoal;
    }

    public boolean isTimesGoal() {
        return timesGoal;
    }

    public int getLinesCount() {
        return linesCount;
    }

    public long getTimeMillisLength() {
        return timeMillisLength;
    }
}