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
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Playfield {
    private static int FIELD_WIDTH = 10;
    private static int FIELD_HEIGHT = 20;
    private static int FIELD_HEIGHT_BUFFER = 10;
    private Mino playerMino;
    private RotatedShape playerMinoRotateData;
    private int playerMinoX;
    private int playerMinoY;
    private Direction playerMinoDirection;
    private ObjectDataGrid<BlockWithConnection> blocks = new ObjectDataGrid<>(FIELD_WIDTH,
            FIELD_HEIGHT + FIELD_HEIGHT_BUFFER);

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
                if (playerMino.isUseTSpinCheck()) {
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
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset - 1)) {
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
     * @return amount of blocks player moved
     */
    public int sonicDropPlayerMino() {
        int shadowYPos = getShadowYPos();
        if (shadowYPos == playerMinoY) {
            return 0;
        }
        int blocksMoved = playerMinoY - shadowYPos;
        setPlayerMinoPos(playerMinoX, shadowYPos);
        return blocksMoved;
    }

    /**
     * @return true if locked inside playfield, false if did not lock inside
     *         playfield
     */
    public boolean lockPlayerMino() {
        writeShapeToColorGrid(
                blocks,
                playerMinoRotateData,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset,
                playerMino.getColor());

        boolean output = false;
        int yOffset = playerMinoY + playerMinoRotateData.yOffset;
        outerLoop: for (int y = 0; y < playerMinoRotateData.getHeight(); y++) {
            for (int x = 0; x < playerMinoRotateData.getWidth(); x++) {
                if (playerMinoRotateData.getAtPos(x, y) && yOffset + y < FIELD_HEIGHT) {
                    output = true;
                    break outerLoop;
                }
            }
        }

        playerMino = null;
        return output;
    }

    private int getShadowYPos() {
        int shadowY = playerMinoY;

        boolean blocksInBounds = false;
        bibcheck: for (int y = 0; y < playerMinoRotateData.getHeight(); y++) {
            for (int x = 0; x < playerMinoRotateData.getWidth(); x++) {
                if (blocks.getAtPos(playerMinoX + playerMinoRotateData.xOffset + x,
                        playerMinoY + playerMinoRotateData.yOffset + y) != null) {
                    blocksInBounds = true;
                    break bibcheck;
                }
            }
        }

        if (!blocksInBounds) {
            fastcheck: for (; 0 < shadowY + playerMinoRotateData.yOffset; shadowY--) {
                for (int x = 0; x < playerMinoRotateData.getWidth(); x++) {
                    if (blocks.getAtPos(playerMinoX + playerMinoRotateData.xOffset + x,
                            shadowY + playerMinoRotateData.yOffset - 1) != null) {
                        break fastcheck;
                    }
                }
            }
        }

        for (;; shadowY--) {
            if (checkShapeCollision(
                    playerMinoRotateData,
                    playerMinoX + playerMinoRotateData.xOffset,
                    shadowY + playerMinoRotateData.yOffset - 1)) {
                break;
            }
        }
        return shadowY;
    }

    public boolean spawnPlayerMino(Mino mino) {
        setPlayerMino(mino);
        boolean spawnSuccess = setPlayerMinoPos((FIELD_WIDTH - mino.getWidth()) / 2,
                FIELD_HEIGHT + 1);
        if (spawnSuccess) {
            movePlayerMino(0, -1);
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
        ObjectDataGrid<BlockWithConnection> renderBlocks = new ObjectDataGrid<>(FIELD_WIDTH,
                FIELD_HEIGHT + FIELD_HEIGHT_BUFFER);
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
        if (playerMino == null) {
            return null;
        }

        ObjectDataGrid<BlockWithConnection> playerBlocks = new ObjectDataGrid<>(playerMinoRotateData.shape.getWidth(),
                playerMinoRotateData.shape.getHeight());
        writeShapeToColorGrid(playerBlocks, playerMinoRotateData, 0, 0, playerMino.getColor());

        return new PlayerRenderData(
                playerBlocks,
                playerMinoX + playerMinoRotateData.xOffset,
                playerMinoY + playerMinoRotateData.yOffset,
                getShadowYPos() + playerMinoRotateData.yOffset);
    }

    public ClearLinesResult clearLines() {
        ArrayList<Integer> rowsClearedIndex = new ArrayList<>();

        for (int row = 0; row < blocks.getHeight() - 1; row++) {
            boolean removeRow = true;
            for (int col = 0; col < blocks.getWidth(); col++) {
                if (blocks.getAtPos(col, row) == null) {
                    removeRow = false;
                    break;
                }
            }
            if (removeRow) {
                rowsClearedIndex.add(row);
            }
        }

        int[] clearLines = new int[rowsClearedIndex.size()];
        for (int i = 0; i < clearLines.length; i++) {
            clearLines[i] = rowsClearedIndex.get(i);
        }

        if (rowsClearedIndex.size() == 0) {
            return new ClearLinesResult(clearLines);
        }

        Queue<Integer> skipQueue = new ArrayDeque<>(rowsClearedIndex);
        Queue<Integer> recalcConnections = new ArrayDeque<>();
        int rowOffset = 0;

        for (int row = 0; row < blocks.getHeight(); row++) {
            boolean skippedMore = false;
            while (!skipQueue.isEmpty() && skipQueue.peek() == row + rowOffset) {
                rowOffset++;
                skipQueue.poll();
                skippedMore = true;
            }
            for (int col = 0; col < blocks.getWidth(); col++) {
                if (row + rowOffset < blocks.getHeight()) {
                    blocks.setAtPos(col, row, blocks.getAtPos(col, row + rowOffset));
                } else {
                    blocks.setAtPos(col, row, null);
                }
            }
            if (skippedMore) {
                recalcConnections.offer(row);
            }
        }
        for (int row : recalcConnections) {
            for (int col = 0; col < blocks.getWidth(); col++) {
                if (blocks.getAtPos(col, row) != null) {
                    blocks.getAtPos(col, row).setConnectionMino(Dir.Down, false);
                }
                if (0 <= row - 1 && blocks.getAtPos(col, row - 1) != null) {
                    blocks.getAtPos(col, row - 1).setConnectionMino(Dir.Up, false);
                }
                updateConnectionsForBlock(col, row);
                updateConnectionsForBlock(col, row - 1);
            }
        }

        return new ClearLinesResult(clearLines);
    }

    public int getPlayerMinoY() {
        return playerMinoY;
    }

    public String getPlayerMinoName() {
        return playerMino.getName();
    }

    public Direction getPlayerMinoDirection() {
        return playerMinoDirection;
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
        for (int i = FIELD_HEIGHT - 3; i < blocks.getHeight(); i++) {
            for (int x = 0; x < blocks.getWidth(); x++) {
                if (blocks.getAtPos(x, i) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
