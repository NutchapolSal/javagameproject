package Tetris;

import java.awt.Graphics;

public class OneMinoPanel extends MinoPanel {
    private Mino mino;
    private MinoColor color;

    public OneMinoPanel(BlockSkinManager blockSkinManager) {
        super(blockSkinManager, 4, 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mino == null) {
            return;
        }

        for (int y = 0; y < mino.getHeight(); y++) {
            for (int x = 0; x < mino.getWidth(); x++) {
                if (mino.getAtPos(x, y)) {
                    paintMinoBlock(g, x, y, color);
                }
            }
        }

    }

    public void setMino(Mino mino) {
        this.mino = mino;
        if (mino == null) {
            return;
        }
        this.color = mino.color;
        setMinoCanvasSize(mino.getWidth(), mino.getHeight());
    }

    public void setMino(Mino mino, MinoColor overrideColor) {
        this.mino = mino;
        if (mino == null) {
            return;
        }
        this.color = overrideColor;
        setMinoCanvasSize(mino.getWidth(), mino.getHeight());
    }
}
