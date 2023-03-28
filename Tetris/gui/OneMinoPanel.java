package Tetris.gui;

import Tetris.BlockWithConnection;
import Tetris.Mino;
import Tetris.MinoColor;
import Tetris.MinoConnector;
import Tetris.ObjectDataGrid;
import java.awt.Graphics;

public class OneMinoPanel extends MinoPanel {
    private ObjectDataGrid<BlockWithConnection> blocks;
    private MinoColor color;

    public OneMinoPanel(BlockSkinManager blockSkinManager) {
        super(blockSkinManager, 4, 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (blocks == null) {
            return;
        }

        for (int y = 0; y < blocks.getHeight(); y++) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                if (blocks.getAtPos(x, y) != null) {
                    paintMinoBlock(g, x, y, blocks.getAtPos(x, y), color);
                }
            }
        }

    }

    public void setMino(Mino mino) {
        if (mino == null) {
            setMino(mino, MinoColor.Gray);
            return;
        }
        setMino(mino, mino.getColor());
    }

    public void setMino(Mino mino, MinoColor overrideColor) {
        if (mino == null) {
            this.blocks = null;
            return;
        }
        this.blocks = MinoConnector.convertMinoToBWC(mino);
        this.color = overrideColor;
        setMinoCanvasSize(mino.getWidth(), mino.getHeight());
    }
}
