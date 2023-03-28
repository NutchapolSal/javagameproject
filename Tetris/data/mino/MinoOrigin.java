package Tetris.data.mino;

public class MinoOrigin {
    public final int x;
    public final int y;
    /**
     * true means origin should be moved by -0.5 for x and y axis
     */
    public final boolean offset;

    public MinoOrigin(int x, int y, boolean offset) {
        this.x = x;
        this.y = y;
        this.offset = offset;
    }
}
