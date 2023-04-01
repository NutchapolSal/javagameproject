package Tetris.data;

import java.util.concurrent.TimeUnit;
import java.util.function.DoubleUnaryOperator;

public abstract class Easer {
    private long startTime = System.nanoTime();
    private boolean easeAToB = true;
    private boolean differentEaseBToA = false;
    private DoubleUnaryOperator easeBToAFunction = x -> x;
    private DoubleUnaryOperator easeAToBFunction = x -> x;

    private long timeLength = TimeUnit.MILLISECONDS.toNanos(1);

    public void setTimeLength(long duration, TimeUnit timeUnit) {
        this.timeLength = timeUnit.toNanos(duration);
    }

    protected double getEaseValue(long currTime) {
        long deltaTime = currTime - startTime;
        if (timeLength <= deltaTime) {
            return easeAToB ? 1.0 : 0.0;
        }
        double rawEaseValue = (double) deltaTime / timeLength;
        if (easeAToB) {
            return easeAToBFunction.applyAsDouble(rawEaseValue);
        } else {
            return 1 - (easeBToAFunction.applyAsDouble(rawEaseValue));
        }
    }

    public void setEaseFunction(DoubleUnaryOperator func) {
        easeAToBFunction = func;
        if (!differentEaseBToA) {
            easeBToAFunction = func;
        }
    }

    public void setEaseBToAFunction(DoubleUnaryOperator func) {
        differentEaseBToA = true;
        easeBToAFunction = func;
    }

    public void unsetEaseBToAFunction() {
        differentEaseBToA = false;
        easeBToAFunction = easeAToBFunction;
    }

    public void startEase(long startTime, boolean easeAToB) {
        this.startTime = startTime;
        this.easeAToB = easeAToB;
    }
}
