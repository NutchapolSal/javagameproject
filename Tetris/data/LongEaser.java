package Tetris.data;

public class LongEaser extends Easer {
    private long valueA;
    private long valueB;

    public void setValueA(long valueA) {
        this.valueA = valueA;
    }

    public void setValueB(long valueB) {
        this.valueB = valueB;
    }

    public long getValue(long currTime) {
        return (long) (valueA + ((valueB - valueA) * getEaseValue(currTime)));
    }
}
