package Tetris;

import Tetris.KickTable.KickTableBuilder;

public class Mino {
    protected BooleanDataGrid shape;
    protected MinoOrigin origin;
    protected KickTable kickTable = (new KickTableBuilder()).build();
    protected MinoColor color = MinoColor.White;

    public Mino(BooleanDataGrid shape, MinoOrigin origin) {
        this.shape = shape;
        this.origin = origin;
    }

    public Mino(BooleanDataGrid shape, MinoOrigin origin, KickTable kickTable) {
        this.shape = shape;
        this.origin = origin;
        this.kickTable = kickTable;
    }

    public Mino(BooleanDataGrid shape, MinoOrigin origin, KickTable kickTable, MinoColor color) {
        this.shape = shape;
        this.origin = origin;
        this.kickTable = kickTable;
        this.color = color;
    }

    public Mino(BooleanDataGrid shape, MinoOrigin origin, MinoColor color) {
        this.shape = shape;
        this.origin = origin;
        this.color = color;
    }

    public boolean getShapeAtPos(int x, int y) {
        return this.shape.getAtPos(x, y);
    }

    public int getShapeWidth() {
        return this.shape.getWidth();
    }

    public int getShapeHeight() {
        return this.shape.getHeight();
    }

    public MinoColor getColor() {
        return this.color;
    }

    public KickTable getKickTable() {
        return kickTable;
    }

    public MinoOrigin getOrigin() {
        return origin;
    }
}
