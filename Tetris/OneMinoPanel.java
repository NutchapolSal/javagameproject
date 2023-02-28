package Tetris;

import java.awt.Graphics;

public class OneMinoPanel extends MinoPanel {
    private Mino mino;

    public OneMinoPanel() {
        super(4, 2);
        int chosenMino = (int) Math.floor(Math.random() * 7);
        switch (chosenMino) {
            case 0:
                setMino(Tetromino.I());

                break;
            case 1:
                setMino(Tetromino.O());

                break;
            case 2:
                setMino(Tetromino.J());

                break;
            case 3:
                setMino(Tetromino.L());

                break;
            case 4:
                setMino(Tetromino.S());

                break;
            case 5:
                setMino(Tetromino.Z());

                break;
            case 6:
                setMino(Tetromino.T());
                break;
            default:
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mino == null) {
            return;
        }

        for (int y = 0; y < mino.getShapeHeight(); y++) {
            for (int x = 0; x < mino.getShapeWidth(); x++) {
                if (mino.getShapeAtPos(x, y)) {
                    paintMinoBlock(g, x, y, mino.getColor());
                }
            }
        }

    }

    public void setMino(Mino mino) {
        this.mino = mino;
        setMinoCanvasSize(mino.getShapeWidth(), mino.getShapeHeight());
    }
}
