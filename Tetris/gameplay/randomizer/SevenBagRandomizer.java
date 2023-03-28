package Tetris.gameplay.randomizer;

import Tetris.data.mino.Mino;
import Tetris.gameplay.Tetromino;

public class SevenBagRandomizer extends MinoRandomizer {

    private final Mino[] bag = new Mino[7];
    private int bagIndex;

    public SevenBagRandomizer(long seed) {
        super(seed);
        fillBag();
        shuffleMinoInBag();
    }

    private void fillBag() {
        bag[0] = Tetromino.I();
        bag[1] = Tetromino.L();
        bag[2] = Tetromino.J();
        bag[3] = Tetromino.O();
        bag[4] = Tetromino.S();
        bag[5] = Tetromino.T();
        bag[6] = Tetromino.Z();
    }

    private void shuffleMinoInBag() {
        for (int i = 0; i < bag.length; i++) {
            int randomIndex = random.nextInt(bag.length - i) + i;
            Mino temp = bag[i];
            bag[i] = bag[randomIndex];
            bag[randomIndex] = temp;
        }
        bagIndex = 0;
    }

    @Override
    public Mino next() {
        if (bagIndex == bag.length) {
            shuffleMinoInBag();
        }
        return bag[bagIndex++];
    }
}
