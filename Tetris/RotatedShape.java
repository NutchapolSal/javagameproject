package Tetris;

class RotatedShape implements ShapeGrid {
    public final ShapeGrid shape;
    /**
     * offset from original shape origin
     */
    public final int xOffset;
    /**
     * offset from original shape origin
     */
    public final int yOffset;

    RotatedShape(ShapeGrid shape, int xCornerOffset, int yCornerOffset) {
        this.shape = shape;
        this.xOffset = xCornerOffset;
        this.yOffset = yCornerOffset;
    }

    @Override
    public boolean getAtPos(int x, int y) {
        return this.shape.getAtPos(x, y);
    }

    @Override
    public int getHeight() {
        return this.shape.getHeight();
    }

    @Override
    public int getWidth() {
        return this.shape.getWidth();
    }

}