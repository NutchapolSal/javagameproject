package Tetris;

public class ObjectDataGrid<T> {
    protected T[][] field;

    @SuppressWarnings("unchecked")
    protected ObjectDataGrid(int w, int h) {
        this.field = (T[][]) new Object[w][h];
    }

    public T getAtPos(int x, int y) {
        return field[x][y];
    }

    public int getWidth() {
        return field.length;
    }

    public int getHeight() {
        return field[0].length;
    }

    public void setAtPos(int x, int y, T input) {
        field[x][y] = input;
    }

}
