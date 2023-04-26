package Tetris.data.mino;

import Tetris.input.Rotation;

public enum Direction {
    Up(0, 1), Right(1, 0), Down(0, -1), Left(-1, 0);

    private final int xOffset;
    private final int yOffset;

    Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    private static final Direction[] vals = values();

    public Direction rotate(Rotation rot) {
        int changeValue;
        switch (rot) {
            case Clockwise:
                changeValue = 1;
                break;
            case Flip:
                changeValue = 2;
                break;
            case CounterClockwise:
                changeValue = 3;
                break;
            default:
                return this;
        }
        return vals[(this.ordinal() + changeValue) % vals.length];

    }
}
