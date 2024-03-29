package Tetris.data.util;

import Tetris.data.BooleanDataGrid;
import Tetris.data.RotatedShape;
import Tetris.data.ShapeGrid;
import Tetris.data.XY;
import Tetris.data.mino.Direction;
import Tetris.data.mino.Mino;
import Tetris.data.mino.MinoOrigin;

public class ShapeRotator {
    private ShapeRotator() {
    };

    public static ShapeGrid getRotatedShape(ShapeGrid shape, Direction dir) {
        BooleanDataGrid newShape;
        int oldWidth = shape.getWidth();
        int oldHeight = shape.getHeight();
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
                newShape.setAtPos(rotated.x, rotated.y, shape.getAtPos(x, y));
            }
        }
        return newShape;
    }

    public static RotatedShape getRotatedMino(Mino mino, Direction dir) {
        ShapeGrid newShape = getRotatedShape(mino, dir);

        MinoOrigin rotOrigin;
        XY rotOriginPoint = rotatePoint(mino.getOrigin().x, mino.getOrigin().y, mino.getWidth(), mino.getHeight(), dir);
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

        return new RotatedShape(newShape, mino.getOrigin().x - rotOrigin.x,
                mino.getOrigin().y - rotOrigin.y);
    }

    /**
     * maps a point x,y in w x h rectangle to a point in rotated w x h rectangle
     * 
     * @param dir rotate direction (up means no rotate)
     * @return position of new point relative to rotated rectangle's bottom left
     *         corner
     */
    private static XY rotatePoint(int x, int y, int w, int h, Direction dir) {
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