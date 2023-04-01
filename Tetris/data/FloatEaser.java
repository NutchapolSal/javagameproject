package Tetris.data;

public class FloatEaser extends Easer {
    private float valueA;
    private float valueB;

    public void setValueA(float valueA) {
        this.valueA = valueA;
    }

    public void setValueB(float valueB) {
        this.valueB = valueB;
    }

    public float getValue(long currTime) {
        return (float) (valueA + ((valueB - valueA) * getEaseValue(currTime)));
    }
}
