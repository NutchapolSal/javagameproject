package Tetris.gameplay;

import Tetris.data.BlockWithConnection;
import Tetris.data.BlockWithConnection.Dir;
import Tetris.data.BooleanDataGrid;
import Tetris.data.ObjectDataGrid;
import Tetris.data.PlayerRenderData;
import Tetris.data.RotatedShape;
import Tetris.data.ShapeGrid;
import Tetris.data.XY;
import Tetris.data.mino.Direction;
import Tetris.data.mino.Mino;
import Tetris.data.mino.MinoColor;
import Tetris.data.util.ShapeRotator;
import Tetris.input.Rotation;

public class Playfield {
    class PlayerData {
        final Mino mino;
        int x;
        int y;
        RotatedShape rotateData;
        Direction direction = Direction.Up;

        public PlayerData(Mino playerMino) {
            this.mino = playerMino;
            rotateData = ShapeRotator.getRotatedMino(playerMino, direction);
        }

    }

    private int fieldWidth;
    private int fieldHeight;
    public static final int FIELD_HEIGHT_BUFFER = 10;
    private PlayerData playerData;
    private ObjectDataGrid<BlockWithConnection> blocks;

    public Playfield(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        blocks = new ObjectDataGrid<>(fieldWidth, fieldHeight + FIELD_HEIGHT_BUFFER);
    }

    public boolean hasPlayerMino() {
        return playerData != null;
    }

    public boolean moveXPlayerMino(int x) {
        return movePlayerMino(x, 0);
    }

    public boolean moveYPlayerMino(int y) {
        return movePlayerMino(0, y);
    }

    public boolean getPlayerMinoGrounded() {
        return checkShapeCollision(playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset,
                playerData.y + playerData.rotateData.yOffset - 1);
    }

    private void setPlayerMino(Mino mino) {
        playerData = new PlayerData(mino);
    }

    private boolean movePlayerMino(int x, int y) {
        return setPlayerMinoPos(playerData.x + x, playerData.y + y);
    }

    private boolean setPlayerMinoPos(int x, int y) {
        boolean collided = checkShapeCollision(playerData.rotateData,
                x + playerData.rotateData.xOffset,
                y + playerData.rotateData.yOffset);
        if (collided) {
            return false;
        }
        playerData.x = x;
        playerData.y = y;
        return true;
    }

    public RotationResult rotatePlayerMino(Rotation rot) {
        Direction beforeRotate = playerData.direction;
        Direction afterRotate = playerData.direction.rotate(rot);

        RotatedShape rotateData = ShapeRotator.getRotatedMino(playerData.mino, afterRotate);
        XY[] kicks = playerData.mino.getKicks(beforeRotate, afterRotate);
        for (XY kick : kicks) {
            boolean collided = checkShapeCollision(rotateData.shape, playerData.x + rotateData.xOffset + kick.x,
                    playerData.y + rotateData.yOffset + kick.y);
            if (!collided) {
                playerData.x = playerData.x + kick.x;
                playerData.y = playerData.y + kick.y;
                playerData.direction = afterRotate;
                playerData.rotateData = rotateData;
                if (playerData.mino.isUseTSpinCheck()) {
                    return threeCornerCheck(kick.x, kick.y);
                } else {
                    return immobileCheck();
                }
            }
        }
        return RotationResult.Fail;
    }

    private RotationResult immobileCheck() {
        if (!checkShapeCollision(playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset,
                playerData.y + playerData.rotateData.yOffset + 1)) {
            return RotationResult.Success;
        }
        if (!checkShapeCollision(playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset,
                playerData.y + playerData.rotateData.yOffset - 1)) {
            return RotationResult.Success;
        }
        if (!checkShapeCollision(playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset + 1,
                playerData.y + playerData.rotateData.yOffset)) {
            return RotationResult.Success;
        }
        if (!checkShapeCollision(playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset - 1,
                playerData.y + playerData.rotateData.yOffset)) {
            return RotationResult.Success;
        }
        return RotationResult.SuccessTwist;
    }

    private RotationResult threeCornerCheck(int kickX, int kickY) {
        BooleanDataGrid absoluteTopLeftCorner = new BooleanDataGrid(3, 3);
        absoluteTopLeftCorner.setAtPos(0, 2, true);

        ShapeGrid relativeTopLeftCorner = ShapeRotator.getRotatedShape(absoluteTopLeftCorner, playerData.direction);
        ShapeGrid relativeTopRightCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Right);
        ShapeGrid relativeBottomRightCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Down);
        ShapeGrid relativeBottomLeftCorner = ShapeRotator.getRotatedShape(relativeTopLeftCorner, Direction.Left);

        int squaresCount = 0;
        int frontSquaresCount = 0;
        if (checkShapeCollision(relativeTopLeftCorner,
                playerData.x + playerData.mino.getOrigin().x - 1,
                playerData.y + playerData.mino.getOrigin().y - 1)) {
            squaresCount++;
            frontSquaresCount++;
        }
        if (checkShapeCollision(relativeTopRightCorner,
                playerData.x + playerData.mino.getOrigin().x - 1,
                playerData.y + playerData.mino.getOrigin().y - 1)) {
            squaresCount++;
            frontSquaresCount++;
        }
        if (checkShapeCollision(relativeBottomLeftCorner,
                playerData.x + playerData.mino.getOrigin().x - 1,
                playerData.y + playerData.mino.getOrigin().y - 1)) {
            squaresCount++;
        }
        if (checkShapeCollision(relativeBottomRightCorner,
                playerData.x + playerData.mino.getOrigin().x - 1,
                playerData.y + playerData.mino.getOrigin().y - 1)) {
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
     * @return amount of blocks player moved
     */
    public int sonicDropPlayerMino() {
        int shadowYPos = getShadowYPos();
        if (shadowYPos == playerData.y) {
            return 0;
        }
        int blocksMoved = playerData.y - shadowYPos;
        setPlayerMinoPos(playerData.x, shadowYPos);
        return blocksMoved;
    }

    /**
     * @return true if locked inside playfield, false if did not lock inside
     *         playfield
     */
    public boolean lockPlayerMino() {
        writeShapeToColorGrid(
                blocks,
                playerData.rotateData,
                playerData.x + playerData.rotateData.xOffset,
                playerData.y + playerData.rotateData.yOffset,
                playerData.mino.getColor());

        boolean output = false;
        int yOffset = playerData.y + playerData.rotateData.yOffset;
        outerLoop: for (int y = 0; y < playerData.rotateData.getHeight(); y++) {
            for (int x = 0; x < playerData.rotateData.getWidth(); x++) {
                if (playerData.rotateData.getAtPos(x, y) && yOffset + y < fieldHeight) {
                    output = true;
                    break outerLoop;
                }
            }
        }

        playerData = null;
        return output;
    }

    private int getShadowYPos() {
        int shadowY = playerData.y;

        boolean blocksInBounds = false;
        bibcheck: for (int y = 0; y < playerData.rotateData.getHeight(); y++) {
            for (int x = 0; x < playerData.rotateData.getWidth(); x++) {
                if (blocks.getAtPos(playerData.x + playerData.rotateData.xOffset + x,
                        playerData.y + playerData.rotateData.yOffset + y) != null) {
                    blocksInBounds = true;
                    break bibcheck;
                }
            }
        }

        if (!blocksInBounds) {
            fastcheck: for (; 0 < shadowY + playerData.rotateData.yOffset; shadowY--) {
                for (int x = 0; x < playerData.rotateData.getWidth(); x++) {
                    if (blocks.getAtPos(playerData.x + playerData.rotateData.xOffset + x,
                            shadowY + playerData.rotateData.yOffset - 1) != null) {
                        break fastcheck;
                    }
                }
            }
        }

        for (;; shadowY--) {
            if (checkShapeCollision(
                    playerData.rotateData,
                    playerData.x + playerData.rotateData.xOffset,
                    shadowY + playerData.rotateData.yOffset - 1)) {
                break;
            }
        }
        return shadowY;
    }

    public boolean spawnPlayerMino(Mino mino) {
        setPlayerMino(mino);
        boolean spawnSuccess = setPlayerMinoPos((fieldWidth - mino.getWidth()) / 2,
                fieldHeight + 1);
        if (spawnSuccess) {
            movePlayerMino(0, -1);
        }
        return spawnSuccess;
    }

    public Mino swapHold(Mino holdMino) {
        PlayerData newHoldMino = playerData;
        spawnPlayerMino(holdMino);
        return newHoldMino.mino;
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

    private static void writeShapeToColorGrid(ObjectDataGrid<BlockWithConnection> blocks, ShapeGrid shape, int x, int y,
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
                BlockWithConnection newBlock = new BlockWithConnection(mc);
                for (var dir : BlockWithConnection.Dir.values()) {
                    if (testX + dir.x() < w && 0 <= testX + dir.x() &&
                            testY + dir.y() < h && 0 <= testY + dir.y() &&
                            shape.getAtPos(testX + dir.x(), testY + dir.y())) {
                        newBlock.setConnectionAll(dir, true);
                        newBlock.setConnectionColor(dir, true);
                        newBlock.setConnectionMino(dir, true);
                        continue;
                    }
                    if (x + testX + dir.x() < 0 ||
                            y + testY + dir.y() < 0 ||
                            blocks.getWidth() <= x + testX + dir.x() ||
                            blocks.getHeight() <= y + testY + dir.y()) {
                        continue;
                    }
                    var testBlock = blocks.getAtPos(x + testX + dir.x(), y + testY + dir.y());
                    if (testBlock == null) {
                        continue;
                    }
                    newBlock.setConnectionAll(dir, true);
                    testBlock.setConnectionAll(dir.opposite(), true);
                    if (testBlock.getMinoColor() == newBlock.getMinoColor()) {
                        newBlock.setConnectionColor(dir, true);
                        testBlock.setConnectionColor(dir.opposite(), true);
                    }
                }
                blocks.setAtPos(testX + x, testY + y, newBlock);
            }
        }
    }

    private void updateConnectionsForBlock(int x, int y) {
        BlockWithConnection block;
        try {
            block = blocks.getAtPos(x, y);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        if (block == null) {
            return;
        }
        for (var dir : BlockWithConnection.Dir.values()) {
            if (x + dir.x() < 0 ||
                    y + dir.y() < 0 ||
                    blocks.getWidth() <= x + dir.x() ||
                    blocks.getHeight() <= y + dir.y()) {
                block.setConnectionAll(dir, false);
                block.setConnectionColor(dir, false);
                block.setConnectionMino(dir, false);
                continue;
            }
            var testBlock = blocks.getAtPos(x + dir.x(), y + dir.y());
            if (testBlock == null) {
                block.setConnectionAll(dir, false);
                block.setConnectionColor(dir, false);
                block.setConnectionMino(dir, false);
                continue;
            }
            block.setConnectionAll(dir, true);
            testBlock.setConnectionAll(dir.opposite(), true);
            boolean isSameColor = testBlock.getMinoColor() == block.getMinoColor();
            block.setConnectionColor(dir, isSameColor);
            testBlock.setConnectionColor(dir.opposite(), isSameColor);
        }
    }

    public ObjectDataGrid<BlockWithConnection> getRenderBlocks() {
        ObjectDataGrid<BlockWithConnection> renderBlocks = new ObjectDataGrid<>(fieldWidth,
                fieldHeight + FIELD_HEIGHT_BUFFER);
        for (int srcY = 0; srcY < renderBlocks.getHeight(); srcY++) {
            for (int srcX = 0; srcX < blocks.getWidth(); srcX++) {
                if (blocks.getAtPos(srcX, srcY) != null) {
                    renderBlocks.setAtPos(srcX, srcY, blocks.getAtPos(srcX, srcY).makeCopy());
                }
            }
        }
        return renderBlocks;
    }

    public PlayerRenderData getPlayerRenderData() {
        if (playerData == null) {
            return null;
        }

        ObjectDataGrid<BlockWithConnection> playerBlocks = new ObjectDataGrid<>(playerData.rotateData.shape.getWidth(),
                playerData.rotateData.shape.getHeight());
        writeShapeToColorGrid(playerBlocks, playerData.rotateData, 0, 0, playerData.mino.getColor());

        return new PlayerRenderData(
                playerBlocks,
                playerData.x + playerData.rotateData.xOffset,
                playerData.y + playerData.rotateData.yOffset,
                getShadowYPos() + playerData.rotateData.yOffset);
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

                for (int x = 0; x < width; x++) {
                    if (blocks.getAtPos(x, row) != null) {
                        blocks.getAtPos(x, row).setConnectionMino(Dir.Down, false);
                    }
                    if (0 <= row - 1 && blocks.getAtPos(x, row - 1) != null) {
                        blocks.getAtPos(x, row - 1).setConnectionMino(Dir.Up, false);
                    }
                    updateConnectionsForBlock(x, row);
                    updateConnectionsForBlock(x, row - 1);
                }
            }
            row--;
        }
        return rowsCleared;
    }

    public int getPlayerMinoY() {
        if (playerData == null) {
            return fieldHeight + 2;
        }
        return playerData.y;
    }

    public String getPlayerMinoName() {
        return playerData.mino.getName();
    }

    public Direction getPlayerMinoDirection() {
        return playerData.direction;
    }

    public boolean isClear() {
        for (int y = 0; y < blocks.getHeight(); y++) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                if (blocks.getAtPos(x, y) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean getDanger() {
        for (int i = fieldHeight - 3; i < blocks.getHeight(); i++) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                if (blocks.getAtPos(x, i) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
