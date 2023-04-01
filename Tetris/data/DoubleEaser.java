package Tetris.data;

public class DoubleEaser extends Easer {
    private double valueA;
    private double valueB;

    public DoubleEaser(long startTime) {
        super(startTime);
    }

    public void setValueA(double valueA) {
        this.valueA = valueA;
    }

    public void setValueB(double valueB) {
        this.valueB = valueB;
    }

    public double getValue(long currTime) {
        return valueA + ((valueB - valueA) * getEaseValue(currTime));
    }

}
