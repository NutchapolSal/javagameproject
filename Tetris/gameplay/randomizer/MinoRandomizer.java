package Tetris.gameplay.randomizer;

import Tetris.data.mino.Mino;
import java.util.Random;

public abstract class MinoRandomizer {
    protected Random random;

    protected MinoRandomizer(long seed) {
        random = new Random(seed);
    }

    public abstract Mino next();
}
