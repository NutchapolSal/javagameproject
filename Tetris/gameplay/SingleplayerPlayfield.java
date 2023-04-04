package Tetris.gameplay;

import Tetris.data.PlayerRenderData;
import Tetris.data.mino.Direction;
import Tetris.data.mino.Mino;
import Tetris.input.Rotation;

public class SingleplayerPlayfield extends Playfield {
    public SingleplayerPlayfield(int fieldWidth, int fieldHeight) {
        super(fieldWidth, fieldHeight, 1);
    }

    public Direction getPlayerMinoDirection() {
        return super.getPlayerMinoDirection(0);
    }

    public boolean getPlayerMinoGrounded() {
        return super.getPlayerMinoGrounded(0);
    }

    public String getPlayerMinoName() {
        return super.getPlayerMinoName(0);
    }

    public int getPlayerMinoY() {
        return super.getPlayerMinoY(0);
    }

    public PlayerRenderData getPlayerRenderData() {
        return super.getPlayerRenderData(0);
    }

    public boolean hasPlayerMino() {
        return super.hasPlayerMino(0);
    }

    public boolean lockPlayerMino() {
        return super.lockPlayerMino(0);
    }

    public boolean moveXPlayerMino(int x) {
        return super.moveXPlayerMino(0, x);
    }

    public boolean moveYPlayerMino(int y) {
        return super.moveYPlayerMino(0, y);
    }

    public RotationResult rotatePlayerMino(Rotation rot) {
        return super.rotatePlayerMino(0, rot);
    }

    public int sonicDropPlayerMino() {
        return super.sonicDropPlayerMino(0);
    }

    public boolean spawnPlayerMino(Mino mino) {
        return super.spawnPlayerMino(0, mino);
    }

    public Mino swapHold(Mino holdMino) {
        return super.swapHold(0, holdMino);
    }

}
