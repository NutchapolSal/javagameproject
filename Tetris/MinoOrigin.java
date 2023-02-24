package Tetris;

public class MinoOrigin {
    public final int x;
    public final int y;
    public final boolean xOffset;
    public final boolean yOffset;

    public MinoOrigin(int x, int y, boolean xOffset, boolean yOffset) {
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}