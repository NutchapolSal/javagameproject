package Tetris;

import java.util.Random;

public abstract class MinoRandomizer {
    protected Random random;

    public MinoRandomizer(long seed) {
        random = new Random(seed);
    }

    public abstract Mino next();
}
