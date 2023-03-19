package Tetris;

public class CalloutTestRandomizer extends MinoRandomizer {
    private int minoIndex = 0;

    private Mino[] stuff = new Mino[4];

    public CalloutTestRandomizer(long seed) {
        super(seed);
        for (int i = 0; i < stuff.length; i++) {
            BooleanDataGrid shape = new BooleanDataGrid(10, i + 1);

            for (int y = 0; y < shape.getHeight(); y++) {
                for (int x = 0; x < shape.getWidth(); x++) {
                    shape.setAtPos(x, y, true);
                }
            }

            stuff[i] = new Mino.MinoBuilder(shape, new MinoOrigin(0, 0, false))
                    .color(MinoColor.Cyan)
                    .build();
        }
    }

    @Override
    public Mino next() {
        Mino currMino = stuff[minoIndex];
        minoIndex = (minoIndex + 1) % stuff.length;
        return currMino;
    }

}
