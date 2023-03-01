package Tetris;

import Tetris.KickTableMap.KickTableBuilder;

public class Mino implements ShapeGrid, KickTable {
    protected ShapeGrid shape;
    protected MinoOrigin origin;
    protected KickTable kickTable;
    protected MinoColor color;
    protected String name;

    public static class MinoBuilder {
        protected ShapeGrid shape;
        protected MinoOrigin origin;
        protected KickTable kickTable = (new KickTableBuilder()).build();
        protected MinoColor color = MinoColor.White;
        protected String name = "";

        public MinoBuilder(ShapeGrid shape, MinoOrigin origin) {
            this.shape = shape;
            this.origin = origin;
        }

        public MinoBuilder kickTable(KickTable kickTable) {
            this.kickTable = kickTable;
            return this;
        }

        public MinoBuilder color(MinoColor color) {
            this.color = color;
            return this;
        }

        public MinoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Mino build() {
            return new Mino(this);
        }
    }

    private Mino(MinoBuilder minoBuilder) {
        this.shape = minoBuilder.shape;
        this.origin = minoBuilder.origin;
        this.kickTable = minoBuilder.kickTable;
        this.color = minoBuilder.color;
        this.name = minoBuilder.name;
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
