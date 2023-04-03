package Tetris.data.easer;

public class IntEaser extends Easer {
    private int valueA;
    private int valueB;

    public IntEaser(long startTime) {
        super(startTime);
    }

    public void setValueA(int valueA) {
        this.valueA = valueA;
    }

    public void setValueB(int valueB) {
        this.valueB = valueB;
    }

    public int getValue(long currTime) {
        return (int) (valueA + ((valueB - valueA) * getEaseValue(currTime)));
    }
}
