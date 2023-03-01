package Tetris;

import Tetris.KickTableMap.KickTableBuilder;

public class Mino implements ShapeGrid, KickTable {
    protected ShapeGrid shape;
    protected MinoOrigin origin;
    protected KickTable kickTable = (new KickTableBuilder()).build();
    protected MinoColor color = MinoColor.White;
    protected String name = "";

    public Mino(ShapeGrid shape, MinoOrigin origin) {
        this.shape = shape;
        this.origin = origin;
    }

    public Mino(ShapeGrid shape, MinoOrigin origin, KickTable kickTable) {
        this.shape = shape;
        this.origin = origin;
        this.kickTable = kickTable;
    }

    public Mino(ShapeGrid shape, MinoOrigin origin, KickTable kickTable, MinoColor color) {
        this.shape = shape;
        this.origin = origin;
        this.kickTable = kickTable;
        this.color = color;
    }

    public Mino(ShapeGrid shape, MinoOrigin origin, MinoColor color) {
        this.shape = shape;
        this.origin = origin;
        this.color = color;
    }

    public Mino(ShapeGrid shape, MinoOrigin origin, KickTable kickTable, MinoColor color, String name) {
        this.shape = shape;
        this.origin = origin;
        this.kickTable = kickTable;
        this.color = color;
        this.name = name;
    }

    public boolean getAtPos(int x, int y) {
        return this.shape.getAtPos(x, y);
    }

    public int getWidth() {
        return this.shape.getWidth();
    }

    public int getHeight() {
        return this.shape.getHeight();
    }

    public MinoColor getColor() {
        return this.color;
    }

    @Override
    public XY[] getKicks(Direction beginDir, Direction destDir) {
        return this.kickTable.getKicks(beginDir, destDir);
    }

    public MinoOrigin getOrigin() {
        return origin;
    }

    /**
     * @return mino's name (empty string if not named)
     */
    public String getName() {
        return name;
    }
}
