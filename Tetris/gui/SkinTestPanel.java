package Tetris.gui;

import Tetris.data.BlockWithConnection;
import Tetris.data.ObjectDataGrid;
import Tetris.gameplay.Playfield;
import Tetris.gameplay.Tetromino;
import Tetris.input.Rotation;

public class SkinTestPanel {
    private PlayfieldPanel playfield;
    private static ObjectDataGrid<BlockWithConnection> renderBlocks = new ObjectDataGrid<>(7, 2);

    static {
        Playfield pf = new Playfield(7, 2, 1);

        pf.spawnPlayerMino(0, Tetromino.J());
        pf.moveXPlayerMino(0, -2);
        pf.sonicDropPlayerMino(0);
        pf.lockPlayerMino(0);

        pf.spawnPlayerMino(0, Tetromino.J());
        pf.moveXPlayerMino(0, -1);
        pf.rotatePlayerMino(0, Rotation.Flip);
        pf.sonicDropPlayerMino(0);
        pf.lockPlayerMino(0);

        pf.spawnPlayerMino(0, Tetromino.T());
        pf.moveXPlayerMino(0, 2);
        pf.sonicDropPlayerMino(0);
        pf.lockPlayerMino(0);

        renderBlocks = pf.getRenderBlocks();
    }

    public SkinTestPanel(BlockSkinManager bsm) {
        playfield = new PlayfieldPanel(bsm);

        playfield.setRenderBlocks(renderBlocks);
    }

    public PlayfieldPanel getPanel() {
        return playfield;
    }

    public void repaint() {
        playfield.repaint();
    }

}
