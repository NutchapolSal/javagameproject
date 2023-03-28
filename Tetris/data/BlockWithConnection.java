package Tetris.data;

import Tetris.data.mino.MinoColor;

public class BlockWithConnection {
    public enum Dir {
        Up(0, 0, 1),
        UpRight(1, 1, 1),
        Right(2, 1, 0),
        DownRight(3, 1, -1),
        Down(4, 0, -1),
        DownLeft(5, -1, -1),
        Left(6, -1, 0),
        UpLeft(7, -1, 1);

        private final int bit;
        private final int x;
        private final int y;

        Dir(int bit, int x, int y) {
            this.bit = bit;
            this.x = x;
            this.y = y;
        }

        public int bit() {
            return bit;
        }

        public int bitValue() {
            return 1 << bit;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public Dir opposite() {
            return Dir.values()[(this.ordinal() + 4) % 8];
        }

        public Dir next() {
            return Dir.values()[(this.ordinal() + 1) % 8];
        }

        public Dir previous() {
            return Dir.values()[(this.ordinal() + 8 - 1) % 8];
        }

        public static Dir[] corners() {
            return new Dir[] { UpRight, DownRight, DownLeft, UpLeft };
        }

        public static Dir[] straights() {
            return new Dir[] { Up, Right, Down, Left };
        }
    }

    private final MinoColor mc;
    private int connectionAll = 0;
    private int connectionColor = 0;
    private int connectionMino = 0;
    private boolean needRevalidate = false;

    public BlockWithConnection(MinoColor mc) {
        this.mc = mc;
    }

    public void setConnectionAll(Dir dir, boolean connect) {
        if (connect) {
            connectionAll |= 1 << dir.bit();
        } else {
            connectionAll &= ~(1 << dir.bit());
        }
        needRevalidate = true;
    }

    public void setConnectionColor(Dir dir, boolean connect) {
        if (connect) {
            connectionColor |= 1 << dir.bit();
        } else {
            connectionColor &= ~(1 << dir.bit());
        }
        needRevalidate = true;
    }

    public void setConnectionMino(Dir dir, boolean connect) {
        if (connect) {
            connectionMino |= 1 << dir.bit();
        } else {
            connectionMino &= ~(1 << dir.bit());
        }
        needRevalidate = true;
    }

    private boolean getBit(int value, Dir dir) {
        return ((value >> dir.bit) & 1) == 1;
    }

    private void revalidate() {
        for (Dir dir : Dir.corners()) {
            if (!(getBit(connectionAll, dir.next()) && getBit(connectionAll, dir.previous()))) {
                setConnectionAll(dir, false);
            }
            if (!(getBit(connectionColor, dir.next()) && getBit(connectionColor, dir.previous()))) {
                setConnectionColor(dir, false);
            }
            if (!(getBit(connectionMino, dir.next()) && getBit(connectionMino, dir.previous()))) {
                setConnectionMino(dir, false);
            }
        }
        needRevalidate = false;
    }

    public MinoColor getMinoColor() {
        return mc;
    }

    public int getConnectionAll() {
        if (needRevalidate) {
            revalidate();
        }
        return connectionAll;
    }

    public int getConnectionColor() {
        if (needRevalidate) {
            revalidate();
        }
        return connectionColor;
    }

    public int getConnectionMino() {
        if (needRevalidate) {
            revalidate();
        }
        return connectionMino;
    }

    public BlockWithConnection makeCopy() {
        BlockWithConnection copy = new BlockWithConnection(this.mc);
        copy.connectionAll = this.connectionAll;
        copy.connectionColor = this.connectionColor;
        copy.connectionMino = this.connectionMino;
        copy.revalidate();
        return copy;
    }

}