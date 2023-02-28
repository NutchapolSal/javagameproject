package Tetris;

public enum Direction {
    Up, Right, Down, Left;

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
