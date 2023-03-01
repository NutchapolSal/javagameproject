package Tetris;

public class Playfield {
    private static int FIELD_WIDTH = 10;
    private static int FIELD_HEIGHT = 20;
    private static int FIELD_HEIGHT_BUFFER = 10;
    private Mino playerMino;
    private RotatedShape playerMinoRotateData;
    // private PlayfieldMinoLocation playerMinoLocation;
    private int playerMinoX;
    private int playerMinoY;
    private Direction playerMinoDirection;
    private ObjectDataGrid<MinoColor> blocks = new ObjectDataGrid<>(FIELD_WIDTH, FIELD_HEIGHT + FIELD_HEIGHT_BUFFER);
    private boolean gameOver;

    public boolean hasPlayerMino() {
        return playerMino != null;
    }

    public boolean moveXPlayerMino(int x) {
        return movePlayerMino(x, 0);
    }

    public void moveYPlayerMino(int y) {
        movePlayerMino(0, y);
    }

    public boolean getPlayerMinoGrounded() {
        return checkShapeCollision(playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset - 1);
    }

    private void setPlayerMino(Mino mino) {
        playerMino = mino;
        playerMinoDirection = Direction.Up;
        playerMinoRotateData = ShapeRotator.getRotatedMino(playerMino, playerMinoDirection);
    }

    private boolean movePlayerMino(int x, int y) {
        return setPlayerMinoPos(playerMinoX + x, playerMinoY + y);
    }

    private boolean setPlayerMinoPos(int x, int y) {
        boolean collided = checkShapeCollision(playerMinoRotateData,
                x + playerMinoRotateData.xOffset,
                y + playerMinoRotateData.yOffset);
        if (collided) {
            return false;
        }
        playerMinoX = x;
        playerMinoY = y;
        return true;
    }

    public boolean rotatePlayerMino(Rotation rot) {
        Direction beforeRotate = playerMinoDirection;
        Direction afterRotate = playerMinoDirection.rotate(rot);

        RotatedShape rotateData = ShapeRotator.getRotatedMino(playerMino, afterRotate);
        XY[] kicks = playerMino.getKicks(beforeRotate, afterRotate);
        for (XY kick : kicks) {
            boolean collided = checkShapeCollision(rotateData.shape, playerMinoX + rotateData.xOffset + kick.x,
                    playerMinoY + rotateData.yOffset + kick.y);
            if (!collided) {
                playerMinoX = playerMinoX + kick.x;
                playerMinoY = playerMinoY + kick.y;
                playerMinoDirection = afterRotate;
                playerMinoRotateData = rotateData;
                return true;
            }
        }
        return false;
    }

    public void sonicDropPlayerMino() {
        setPlayerMinoPos(playerMinoX, getShadowYPos());
    }

    public void lockPlayerMino() {
        writeShapeToColorGrid(
                blocks,
                playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset,
                playerMino.color);
        playerMino = null;
    }

    private int getShadowYPos() {
        int resultY;
        for (int y = playerMinoY;; y--) {
            boolean collided = checkShapeCollision(
                    playerMinoRotateData,
                    playerMinoX + playerMinoRotateData.xOffset,
                    y + playerMinoRotateData.yOffset);
            if (collided) {
                resultY = y + 1;
                break;
            }
        }
        return resultY;
    }

    public boolean spawnPlayerMino(Mino mino) {
        setPlayerMino(mino);
        boolean spawnSuccess = setPlayerMinoPos((FIELD_WIDTH - mino.getWidth()) / 2,
                FIELD_HEIGHT + 1);
        if (spawnSuccess) {
            movePlayerMino(0, -1);
        } else {
            gameOver = true;
        }
        return spawnSuccess;
    }

    public Mino swapHold(Mino holdMino) {
        Mino newHoldMino = playerMino;
        spawnPlayerMino(holdMino);
        return newHoldMino;
    }

    // private boolean checkPlayerCollision(int x, int y, Direction dir) {
    // return false;
    // }

    private boolean checkShapeCollision(ShapeGrid shape, int x, int y) {
        int w = shape.getWidth();
        int h = shape.getHeight();
        for (int testY = 0; testY < h; testY++) {
            for (int testX = 0; testX < w; testX++) {
                if (!shape.getAtPos(testX, testY)) {
                    continue;
                }
                if (testX + x < 0
                        || testY + y < 0
                        || blocks.getWidth() <= testX + x
                        || blocks.getHeight() <= testY + y) {
                    return true;
                }
                if (blocks.getAtPos(testX + x, testY + y) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    static private void writeShapeToColorGrid(ObjectDataGrid<MinoColor> blocks, ShapeGrid shape, int x, int y,
            MinoColor mc) {
        int w = shape.getWidth();
        int h = shape.getHeight();
        for (int testY = 0; testY < h; testY++) {
            for (int testX = 0; testX < w; testX++) {
                if (!shape.getAtPos(testX, testY)) {
                    continue;
                }
                if (testX + x < 0
                        || testY + y < 0
                        || blocks.getWidth() <= testX + x
                        || blocks.getHeight() <= testY + y) {
                    continue;
                }
                blocks.setAtPos(testX + x, testY + y, mc);
            }
        }
    }

    public ObjectDataGrid<MinoColor> getRenderBlocks() {
        ObjectDataGrid<MinoColor> renderBlocks = new ObjectDataGrid<>(FIELD_WIDTH, FIELD_HEIGHT + FIELD_HEIGHT_BUFFER);
        for (int srcY = 0; srcY < renderBlocks.getHeight(); srcY++) {
            for (int srcX = 0; srcX < blocks.getWidth(); srcX++) {
                renderBlocks.setAtPos(srcX, srcY, blocks.getAtPos(srcX, srcY));
            }
        }
        if (hasPlayerMino()) {
            writeShapeToColorGrid(
                    renderBlocks,
                    playerMinoRotateData,
                    playerMinoX + playerMinoRotateData.xOffset,
                    getShadowYPos() + playerMinoRotateData.yOffset,
                    MinoColor.Gray);
            writeShapeToColorGrid(
                    renderBlocks,
                    playerMinoRotateData,
                    playerMinoX + playerMinoRotateData.xOffset,
                    playerMinoY + playerMinoRotateData.yOffset,
                    playerMino.getColor());
        }
        return renderBlocks;
    }

    public int clearLines(){
        int row = FIELD_HEIGHT - 1;
        int rowsCleared = 0;
        boolean removeRow;
        ObjectDataGrid <Boolean> obj = new ObjectDataGrid<>(FIELD_WIDTH, FIELD_HEIGHT);

        while (row >= 0){
            removeRow = true;
            for(int col = 0; col < FIELD_WIDTH; col++){
                if(!(obj.getAtPos(row, col))){
                    removeRow = false;
                    break;
                }
            }

            if(removeRow){
                for (int row1 = row; row1 > 0; row1--){
                    for(int col1 = 0; col1 < FIELD_WIDTH; col1++){
                        obj.getAtPos(row1, col1) = obj.getAtPos(row1-1, col1);
                    }
                }
                rowsCleared++;
                for(int col = 0; col < FIELD_WIDTH; col++){
                    obj.getAtPos(0, col);
                }
            }
        }

    public void magic() {
        blocks.setAtPos(1, 0, MinoColor.White);
        blocks.setAtPos(0, 1, MinoColor.White);
        blocks.setAtPos(1, 1, MinoColor.White);
        blocks.setAtPos(2, 1, MinoColor.White);
        blocks.setAtPos(3, 1, MinoColor.White);
        blocks.setAtPos(4, 1, MinoColor.White);
        blocks.setAtPos(5, 1, MinoColor.White);
        blocks.setAtPos(6, 1, MinoColor.White);
        blocks.setAtPos(7, 1, MinoColor.White);
        blocks.setAtPos(8, 1, MinoColor.White);
        blocks.setAtPos(9, 1, MinoColor.White);
        blocks.setAtPos(5, 2, MinoColor.White);
        blocks.setAtPos(7, 2, MinoColor.White);

        for (int y = blocks.getHeight() - 1; 0 <= y; y--) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                System.out.print(blocks.getAtPos(x, y) == null ? "." : "O");
            }
            System.out.println();
        }

        System.out.printf("lines cleared %s%n", clearLines());

        for (int y = blocks.getHeight() - 1; 0 <= y; y++) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                System.out.print(blocks.getAtPos(x, y) == null ? "." : "O");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Playfield pf = new Playfield();
        pf.magic();
    }

}
