package Tetris;

public class Playfield {
    private static int FIELD_WIDTH = 10;
    private static int FIELD_HEIGHT = 20;
    private Mino playerMino;
    private RotatedShape playerMinoRotateData;
    // private PlayfieldMinoLocation playerMinoLocation;
    private int playerMinoX;
    private int playerMinoY;
    private Direction playerMinoDirection;
    private ObjectDataGrid<MinoColor> blocks;

    public void movePlayerMino(int x, int y) {

    }

    public boolean rotatePlayerMino(Rotation rot) {
        Direction beforeRotate = playerMinoDirection;
        Direction afterRotate = playerMinoDirection.rotate(rot);

        XY[] kicks = playerMino.getKickTable().getKicks(beforeRotate, afterRotate);
        for (XY kick : kicks) {
            boolean collided = checkCollision(playerMinoX + kick.x, playerMinoY + kick.y,
                    afterRotate);
            if (!collided) {
                playerMinoX = playerMinoX + kick.x;
                playerMinoY = playerMinoY + kick.y;
                playerMinoDirection = afterRotate;
            }
        }

        return false;
    }

    public void spawnPlayerMino(Mino mino) {
        playerMino = mino;
        playerMinoX = (FIELD_WIDTH - mino.getShapeWidth()) / 2;
        playerMinoY = FIELD_HEIGHT + mino.getShapeHeight();
        playerMinoDirection = Direction.Up;
    }

    // private boolean checkPlayerCollision(int x, int y, Direction dir) {
    // return false;
    // }

    private boolean checkShapeCollision(BooleanDataGrid shape, int x, int y) {
        int w = shape.getWidth();
        int h = shape.getHeight();
        for (int testY = 0; testY < h; testY++) {
            for (int testX = 0; testX < w; testX++) {
                if (!shape.getAtPos(testX, testY)) {
                    continue;
                }

            }
        }
    }

    public static void main(String[] args) {
        Mino aMino = Tetromino.J();
        for (int y = aMino.getShapeHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < aMino.getShapeWidth(); x++) {
                System.out.print(aMino.getShapeAtPos(x, y) ? "O" : " ");
            }
            System.out.println();
        }

        System.out.println();

        RotatedShape rs = ShapeRotator.getRotatedShape(aMino, Direction.Up);
        for (int y = rs.shape.getHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < rs.shape.getWidth(); x++) {
                System.out.print(rs.shape.getAtPos(x, y) ? "O" : " ");
            }
            System.out.println();
        }
        System.out.printf("%s %s%n%n", rs.xOffset, rs.yOffset);

        rs = ShapeRotator.getRotatedShape(aMino, Direction.Right);
        for (int y = rs.shape.getHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < rs.shape.getWidth(); x++) {
                System.out.print(rs.shape.getAtPos(x, y) ? "O" : " ");
            }
            System.out.println();
        }
        System.out.printf("%s %s%n%n", rs.xOffset, rs.yOffset);

        rs = ShapeRotator.getRotatedShape(aMino, Direction.Down);
        for (int y = rs.shape.getHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < rs.shape.getWidth(); x++) {
                System.out.print(rs.shape.getAtPos(x, y) ? "O" : " ");
            }
            System.out.println();
        }
        System.out.printf("%s %s%n%n", rs.xOffset, rs.yOffset);

        rs = ShapeRotator.getRotatedShape(aMino, Direction.Left);
        for (int y = rs.shape.getHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < rs.shape.getWidth(); x++) {
                System.out.print(rs.shape.getAtPos(x, y) ? "O" : " ");
            }
            System.out.println();
        }
        System.out.printf("%s %s%n%n", rs.xOffset, rs.yOffset);
    }

}
