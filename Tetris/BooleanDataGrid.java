package Tetris;

public class BooleanDataGrid implements ShapeGrid {
    public boolean[][] field;

    public BooleanDataGrid(int w, int h) {
        this.field = new boolean[w][h];
    }

    public boolean getAtPos(int x, int y) {
        return field[x][y];
    }

    public int getWidth() {
        return field.length;
    }

    public int getHeight() {
        return field[0].length;
    }

    public void setAtPos(int x, int y, boolean input) {
        field[x][y] = input;
    }

}
