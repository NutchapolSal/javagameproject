package Tetris;

import Tetris.KickTable.KickTableBuilder;

public class Tetromino {

    static private KickTable commonKickTable;
    static private KickTable iKickTable;

    static private KickTable getCommonKickTable() {
        if (commonKickTable != null) {
            return commonKickTable;
        }

        KickTableBuilder ktb = new KickTableBuilder();
        ktb.setKicks(Direction.Up, Direction.Right,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(-1, 1), new XY(0, -2), new XY(-1, -2) });
        ktb.setKicks(Direction.Right, Direction.Up,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(1, -1), new XY(0, 2), new XY(1, 2) });
        ktb.setKicks(Direction.Right, Direction.Down,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(1, -1), new XY(0, 2), new XY(1, 2) });
        ktb.setKicks(Direction.Down, Direction.Right,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(-1, 1), new XY(0, -2), new XY(-1, -2) });
        ktb.setKicks(Direction.Down, Direction.Left,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(1, 1), new XY(0, -2), new XY(1, -2) });
        ktb.setKicks(Direction.Left, Direction.Down,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(-1, -1), new XY(0, 2), new XY(-1, 2) });
        ktb.setKicks(Direction.Left, Direction.Up,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(-1, -1), new XY(0, 2), new XY(-1, 2) });
        ktb.setKicks(Direction.Up, Direction.Left,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(1, 1), new XY(0, -2), new XY(1, -2) });

        iKickTable = ktb.build();
        return iKickTable;
    }

    static private KickTable getIKickTable() {
        if (iKickTable != null) {
            return iKickTable;
        }

        KickTableBuilder ktb = new KickTableBuilder();
        ktb.setKicks(Direction.Up, Direction.Right,
                new XY[] { new XY(0, 0), new XY(-2, 0), new XY(1, 0), new XY(-2, -1), new XY(1, 2) });
        ktb.setKicks(Direction.Right, Direction.Up,
                new XY[] { new XY(0, 0), new XY(2, 0), new XY(-1, 0), new XY(2, 1), new XY(-1, -2) });
        ktb.setKicks(Direction.Right, Direction.Down,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(2, 0), new XY(-1, 2), new XY(2, -1) });
        ktb.setKicks(Direction.Down, Direction.Right,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(-2, 0), new XY(1, -2), new XY(-2, 1) });
        ktb.setKicks(Direction.Down, Direction.Left,
                new XY[] { new XY(0, 0), new XY(2, 0), new XY(-1, 0), new XY(2, 1), new XY(-1, -2) });
        ktb.setKicks(Direction.Left, Direction.Down,
                new XY[] { new XY(0, 0), new XY(-2, 0), new XY(1, 0), new XY(-2, -1), new XY(1, 2) });
        ktb.setKicks(Direction.Left, Direction.Up,
                new XY[] { new XY(0, 0), new XY(1, 0), new XY(-2, 0), new XY(1, -2), new XY(-2, 1) });
        ktb.setKicks(Direction.Up, Direction.Left,
                new XY[] { new XY(0, 0), new XY(-1, 0), new XY(2, 0), new XY(-1, 2), new XY(2, -1) });

        iKickTable = ktb.build();
        return iKickTable;
    }

    static public Mino I() {
        BooleanDataGrid shape = new BooleanDataGrid(4, 1);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(2, 0, true);
        shape.setAtPos(3, 0, true);

        return new Mino(shape, new MinoOrigin(2, 0, true), getIKickTable(), MinoColor.Cyan);
    }

    static public Mino O() {
        BooleanDataGrid shape = new BooleanDataGrid(2, 2);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(0, 1, true);
        shape.setAtPos(1, 1, true);

        return new Mino(shape, new MinoOrigin(1, 1, true), MinoColor.Yellow);
    }

    static public Mino S() {
        BooleanDataGrid shape = new BooleanDataGrid(3, 2);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(1, 1, true);
        shape.setAtPos(2, 1, true);

        return new Mino(shape, new MinoOrigin(1, 0, false), getCommonKickTable(), MinoColor.Green);
    }

    static public Mino Z() {
        BooleanDataGrid shape = new BooleanDataGrid(3, 2);

        shape.setAtPos(1, 0, true);
        shape.setAtPos(2, 0, true);
        shape.setAtPos(0, 1, true);
        shape.setAtPos(1, 1, true);

        return new Mino(shape, new MinoOrigin(1, 0, false), getCommonKickTable(), MinoColor.Red);
    }

    static public Mino J() {
        BooleanDataGrid shape = new BooleanDataGrid(3, 2);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(2, 0, true);
        shape.setAtPos(0, 1, true);

        return new Mino(shape, new MinoOrigin(1, 0, false), getCommonKickTable(), MinoColor.Blue);
    }

    static public Mino L() {
        BooleanDataGrid shape = new BooleanDataGrid(3, 2);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(2, 0, true);
        shape.setAtPos(2, 1, true);

        return new Mino(shape, new MinoOrigin(1, 0, false), getCommonKickTable(), MinoColor.Orange);
    }

    static public Mino T() {
        BooleanDataGrid shape = new BooleanDataGrid(3, 2);

        shape.setAtPos(0, 0, true);
        shape.setAtPos(1, 0, true);
        shape.setAtPos(2, 0, true);
        shape.setAtPos(1, 1, true);

        return new Mino(shape, new MinoOrigin(1, 0, false), getCommonKickTable(), MinoColor.Purple);
    }
}
