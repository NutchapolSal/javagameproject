package Tetris.gameplay.randomizer;

import Tetris.data.mino.Mino;

public class OneOnlyRandomizer extends SevenBagRandomizer {

    private Mino myMino;

    public OneOnlyRandomizer(long seed) {
        super(seed);
        myMino = super.next();
    }

    @Override
    public Mino next() {
        return myMino;
    }
}
