package Tetris.data.easer;

import java.util.concurrent.TimeUnit;
import java.util.function.DoubleUnaryOperator;

public abstract class Easer {
    private long startTime = System.nanoTime();
    private boolean easeAToB = false;
    private boolean differentEaseBToA = false;
    private DoubleUnaryOperator easeBToAFunction = x -> x;
    private DoubleUnaryOperator easeAToBFunction = x -> x;
    private boolean differentLengthBToA = false;
    private long timeLengthAToB = TimeUnit.MILLISECONDS.toNanos(1);
    private long timeLengthBToA = TimeUnit.MILLISECONDS.toNanos(1);

    protected Easer(long startTime) {
        this.startTime = startTime;
    }

    private void updateStartTime(long oldTimeLength, long currTime) {
        double rawEaseValue = (double) (currTime - startTime) / (oldTimeLength);
        rawEaseValue = Math.max(0.0, Math.min(rawEaseValue, 1.0));

        if (easeAToB) {
            this.startTime = currTime - (long) (timeLengthAToB * rawEaseValue);
        } else {
            this.startTime = currTime - (long) (timeLengthBToA * rawEaseValue);
        }
    }

    public void setTimeLength(long duration, TimeUnit timeUnit) {
        long currTime = System.nanoTime();
        long oldTimeLength = this.timeLengthAToB;
        this.timeLengthAToB = timeUnit.toNanos(duration);
        if (!differentLengthBToA) {
            this.timeLengthBToA = timeUnit.toNanos(duration);
        }
        if (easeAToB || !differentLengthBToA) {
            updateStartTime(oldTimeLength, currTime);
        }
    }

    public void setTimeLengthBToA(long duration, TimeUnit timeUnit) {
        long currTime = System.nanoTime();
        long oldTimeLength = this.timeLengthBToA;

        differentLengthBToA = true;
        this.timeLengthBToA = timeUnit.toNanos(duration);

        if (!easeAToB) {
            updateStartTime(oldTimeLength, currTime);
        }
    }

    public void unsetTimeLengthBToA() {
        differentLengthBToA = false;
        setTimeLength(timeLengthAToB, TimeUnit.NANOSECONDS);
    }

    protected double getEaseValue(long currTime) {
        long currTimeLength = easeAToB ? timeLengthAToB : timeLengthBToA;
        long deltaTime = currTime - startTime;
        if (currTimeLength <= deltaTime) {
            return easeAToB ? 1.0 : 0.0;
        }
        if (deltaTime <= 0) {
            return easeAToB ? 0.0 : 1.0;
        }
        double rawEaseValue = (double) deltaTime / currTimeLength;
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
