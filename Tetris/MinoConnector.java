package Tetris;

public class MinoConnector {
    public static ObjectDataGrid<BlockWithConnection> convertMinoToBWC(Mino mino) {
        ObjectDataGrid<BlockWithConnection> blocks = new ObjectDataGrid<>(mino.getWidth(), mino.getHeight());
        for (int y = 0; y < mino.getHeight(); y++) {
            for (int x = 0; x < mino.getWidth(); x++) {
                if (!mino.getAtPos(x, y)) {
                    continue;
                }
                BlockWithConnection newBlock = new BlockWithConnection(mino.getColor());
                for (var dir : BlockWithConnection.Dir.values()) {
                    if (x + dir.x() < 0 ||
                            y + dir.y() < 0 ||
                            mino.getWidth() <= x + dir.x() ||
                            mino.getHeight() <= y + dir.y()) {
                        continue;
                    }
                    var testBlock = blocks.getAtPos(x + dir.x(), y + dir.y());
                    if (testBlock == null) {
                        continue;
                    }
                    newBlock.setConnectionMino(dir, true);
                    testBlock.setConnectionMino(dir.opposite(), true);
                    newBlock.setConnectionColor(dir, true);
                    testBlock.setConnectionColor(dir.opposite(), true);
                    newBlock.setConnectionAll(dir, true);
                    testBlock.setConnectionAll(dir.opposite(), true);
                }
                blocks.setAtPos(x, y, newBlock);
            }
        }
        return blocks;
    }
}
