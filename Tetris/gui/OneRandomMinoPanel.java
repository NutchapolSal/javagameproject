package Tetris.gui;

import Tetris.data.mino.Mino;
import Tetris.gameplay.Tetromino;

public class OneRandomMinoPanel extends OneMinoPanel {
    public OneRandomMinoPanel(BlockSkinManager blockSkinManager) {
        super(blockSkinManager);
        Mino[] arrOfTetros = new Mino[] { Tetromino.I(), Tetromino.J(), Tetromino.L(), Tetromino.O(), Tetromino.S(),
                Tetromino.T(), Tetromino.Z() };
        Mino chosenMino = arrOfTetros[(int) Math.floor(Math.random() * arrOfTetros.length)];
        setMino(chosenMino);
    }
}
