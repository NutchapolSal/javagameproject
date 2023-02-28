package Tetris;

public class ShapeRotator {
    static public RotatedShape getRotatedShape(Mino mino, Direction dir) {
        BooleanDataGrid newShape;
        int oldWidth = mino.getShapeWidth();
        int oldHeight = mino.getShapeHeight();
        switch (dir) {
            case Left:
            case Right:
                newShape = new BooleanDataGrid(oldHeight, oldWidth);
                break;
            default:
                newShape = new BooleanDataGrid(oldWidth, oldHeight);
                break;
        }

        for (int y = 0; y < oldHeight; y++) {
            for (int x = 0; x < oldWidth; x++) {
                XY rotated = rotatePoint(x, y, oldWidth, oldHeight, dir);
                newShape.setAtPos(rotated.x, rotated.y, mino.getShapeAtPos(x, y));
            }
        }

        MinoOrigin rotOrigin;
        XY rotOriginPoint = rotatePoint(mino.getOrigin().x, mino.getOrigin().y, oldWidth, oldHeight, dir);
        if (mino.getOrigin().offset) {
            switch (dir) {
                case Right:
                    rotOrigin = new MinoOrigin(rotOriginPoint.x, rotOriginPoint.y + 1, true);
                    break;
                case Down:
                    rotOrigin = new MinoOrigin(rotOriginPoint.x + 1, rotOriginPoint.y + 1, true);
                    break;
                case Left:
                    rotOrigin = new MinoOrigin(rotOriginPoint.x + 1, rotOriginPoint.y, true);
                    break;
                default:
                    rotOrigin = new MinoOrigin(rotOriginPoint.x, rotOriginPoint.y, true);
                    break;
            }
        } else {
            rotOrigin = new MinoOrigin(rotOriginPoint.x, rotOriginPoint.y, false);
        }

        RotatedShape data = new RotatedShape(newShape, mino.getOrigin().x - rotOrigin.x,
                mino.getOrigin().y - rotOrigin.y);
        return data;
    }

    /**
     * maps a point x,y in w x h rectangle to a point in rotated w x h rectangle
     * 
     * @param dir rotate direction (up means no rotate)
     * @return position of new point relative to rotated rectangle's bottom left
     *         corner
     */
    static private XY rotatePoint(int x, int y, int w, int h, Direction dir) {
        int newX;
        int newY;
        switch (dir) {
            case Right:
                newX = y;
                newY = (w - 1) - x;
                break;
            case Down:
                newX = (w - 1) - x;
                newY = (h - 1) - y;
                break;
            case Left:
                newX = (h - 1) - y;
                newY = x;
                break;
            default:
                newX = x;
                newY = y;
                break;
        }
        return new XY(newX, newY);
    }
}

class RotatedShape {
    public final BooleanDataGrid shape;
    /**
     * offset from original shape origin
     */
    public final int xOffset;
    /**
     * offset from original shape origin
     */
    public final int yOffset;

    RotatedShape(BooleanDataGrid shape, int xCornerOffset, int yCornerOffset) {
        this.shape = shape;
        this.xOffset = xCornerOffset;
        this.yOffset = yCornerOffset;
    }
}