package Tetris;

public class Playfield {
    private static int FIELD_WIDTH = 10;
    private static int FIELD_HEIGHT = 20;
    private static int FIELD_HEIGHT_BUFFER = 10;
    private Mino playerMino;
    private RotatedShape playerMinoRotateData;
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

    public boolean moveYPlayerMino(int y) {
        return movePlayerMino(0, y);
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

    public RotationResult rotatePlayerMino(Rotation rot) {
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
                if (playerMino.useTSpinCheck) {
                    return threeCornerCheck(kick.x, kick.y);
                } else {
                    return immobileCheck();
                }
            }
        }
        return RotationResult.Fail;
    }

    private RotationResult immobileCheck() {
        if (!checkShapeCollision(playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset + 1)) {
            return RotationResult.Success;
        }
        if (!checkShapeCollision(playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset + 1,
                playerMinoY + playerMinoRotateData.yOffset)) {
            return RotationResult.Success;
        }
        if (!checkShapeCollision(playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset - 1,
                playerMinoY + playerMinoRotateData.yOffset)) {
            return RotationResult.Success;
        }
        return RotationResult.SuccessTwist;
    }

    private RotationResult threeCornerCheck(int kickX, int kickY) {
        BooleanDataGrid absoluteTopLeftCorner = new BooleanDataGrid(3, 3);
        absoluteTopLeftCorner.setAtPos(0, 2, true);

        ShapeGrid relativeTopLeftCorner = ShapeRotator.getRotatedShape(absoluteTopLeftCorner, playerMinoDirection);
        ShapeGrid relativeTopRightCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Right);
        ShapeGrid relativeBottomRightCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Down);
        ShapeGrid relativeBottomLeftCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Left);

        int squaresCount = 0;
        int frontSquaresCount = 0;
        if (checkShapeCollision(relativeTopLeftCorner,
                playerMinoX + playerMino.getOrigin().x - 1,
                playerMinoY + playerMino.getOrigin().y - 1)) {
            squaresCount++;
            frontSquaresCount++;
        }
        if (checkShapeCollision(relativeTopRightCorner,
                playerMinoX + playerMino.getOrigin().x - 1,
                playerMinoY + playerMino.getOrigin().y - 1)) {
            squaresCount++;
            frontSquaresCount++;
        }
        if (checkShapeCollision(relativeBottomLeftCorner,
                playerMinoX + playerMino.getOrigin().x - 1,
                playerMinoY + playerMino.getOrigin().y - 1)) {
            squaresCount++;
        }
        if (checkShapeCollision(relativeBottomRightCorner,
                playerMinoX + playerMino.getOrigin().x - 1,
                playerMinoY + playerMino.getOrigin().y - 1)) {
            squaresCount++;
        }

        if (3 <= squaresCount) {
            if (frontSquaresCount == 2) {
                return RotationResult.SuccessTSpin;
            }
            if (Math.abs(kickX) == 1 && Math.abs(kickY) == 2) {
                return RotationResult.SuccessTSpin;
            }
            return RotationResult.SuccessTSpinMini;
        }
        return RotationResult.Success;
    }

    /**
     * @return true if player mino moved, false if player mino did not move
     */
    public boolean sonicDropPlayerMino() {
        int shadowYPos = getShadowYPos();
        if (shadowYPos == playerMinoY) {
            return false;
        }
        setPlayerMinoPos(playerMinoX, shadowYPos);
        return true;
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

    private static void writeShapeToColorGrid(ObjectDataGrid<MinoColor> blocks, ShapeGrid shape, int x, int y,
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
        return renderBlocks;
    }

    public PlayerRenderData getPlayerRenderData() {
        if (playerMino == null) {
            return null;
        }

        ObjectDataGrid<MinoColor> playerBlocks = new ObjectDataGrid<>(playerMinoRotateData.shape.getWidth(),
                playerMinoRotateData.shape.getHeight());
        writeShapeToColorGrid(playerBlocks, playerMinoRotateData, 0, 0, playerMino.getColor());

        return new PlayerRenderData(
                playerBlocks,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset,
                getShadowYPos() + playerMinoRotateData.yOffset);
    }

    public int clearLines() {
        int row = blocks.getHeight() - 1;
        int width = blocks.getWidth();
        int rowsCleared = 0;
        boolean removeRow;

        while (row >= 0) {
            removeRow = true;
            for (int col = 0; col < width; col++) {
                if (blocks.getAtPos(col, row) == null) {
                    removeRow = false;
                    break;
                }
            }

            if (removeRow) {
                for (int copyRow = row; copyRow < blocks.getHeight() - 1; copyRow++) {
                    for (int copyCol = 0; copyCol < width; copyCol++) {
                        blocks.setAtPos(copyCol, copyRow, blocks.getAtPos(copyCol, copyRow + 1));
                    }
                }

                rowsCleared++;
                for (int col = 0; col < width; col++) {
                    blocks.setAtPos(col, blocks.getHeight() - 1, null);
                }
            } else {
                row--;
            }
        }
        return rowsCleared;
    }

    public int getPlayerMinoY() {
        return playerMinoY;
    }

    public String getPlayerMinoName() {
        return playerMino.getName();
    }

}
