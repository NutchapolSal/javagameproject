package Tetris.data;

public class XY {
    public final int x;
    public final int y;

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "XY [x=" + x + ", y=" + y + "]";
    }
}
