package Tetris.data.easer;

import java.util.function.DoubleUnaryOperator;

public class EasingFunctions {
    private EasingFunctions() {
    }

    public static DoubleUnaryOperator linear() {
        return x -> x;
    }

    public static DoubleUnaryOperator easeOutSine() {
        return x -> Math.sin((x * Math.PI) / 2.0);
    }

    public static DoubleUnaryOperator easeInSine() {
        return x -> 1 - Math.cos((x * Math.PI) / 2.0);
    }

    public static DoubleUnaryOperator easeInOutSine() {
        return x -> -(Math.cos(Math.PI * x) - 1) / 2;
    }

    public static DoubleUnaryOperator easeInPower(double power) {
        return x -> Math.pow(x, power);
    }

    public static DoubleUnaryOperator easeOutPower(double power) {
        return x -> 1 - Math.pow((1 - x), power);
    }

    public static DoubleUnaryOperator easeInOutPower(double power) {
        return x -> x < 0.5 ? 2 * Math.pow(x, power) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

    public static DoubleUnaryOperator easeInExpo() {
        return x -> x <= 0 ? 0 : Math.pow(2, 10 * x - 10);
    }

    public static DoubleUnaryOperator easeOutExpo() {
        return x -> 1.0 <= x ? 1.0 : 1 - Math.pow(2, -10 * x);
    }

    public static DoubleUnaryOperator easeInOutExpo() {
        return x -> {
            if (x <= 0) {
                return 0;
            }
            if (1 <= x) {
                return 1;
            }
            return x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                    : (2 - Math.pow(2, -20 * x + 10)) / 2;
        };
    }
}
