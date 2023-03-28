package Tetris.data.mino;

import Tetris.data.XY;

public interface KickTable {
    public XY[] getKicks(Direction beginDir, Direction destDir);
}
