package Tetris;

import java.awt.Graphics;

public class OneMinoPanel extends MinoPanel {
    private Mino mino;
    private MinoColor color;

    public OneMinoPanel() {
        super(4, 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mino == null) {
            return;
        }

        for (int y = 0; y < mino.getShapeHeight(); y++) {
            for (int x = 0; x < mino.getShapeWidth(); x++) {
                if (mino.getShapeAtPos(x, y)) {
                    paintMinoBlock(g, x, y, color);
                }
            }
        }

    }

    public void setMino(Mino mino) {
        this.mino = mino;
        this.color = mino.color;
        setMinoCanvasSize(mino.getShapeWidth(), mino.getShapeHeight());
    }

    public void setMino(Mino mino, MinoColor overrideColor) {
        this.mino = mino;
        this.color = overrideColor;
        setMinoCanvasSize(mino.getShapeWidth(), mino.getShapeHeight());
    }
}
