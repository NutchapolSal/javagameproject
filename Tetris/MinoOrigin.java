package Tetris;

public class MinoOrigin {
    public final int x;
    public final int y;
    /**
     * true means origin should be moved by -0.5 for x axis
     */
    public final boolean xOffset;
    /**
     * true means origin should be moved by -0.5 for y axis
     */
    public final boolean yOffset;

    public MinoOrigin(int x, int y, boolean xOffset, boolean yOffset) {
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
