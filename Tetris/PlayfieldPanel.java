package Tetris;

import java.awt.Graphics;
import javax.swing.BorderFactory;
import java.awt.Color;

public class PlayfieldPanel extends MinoPanel {
    private ObjectDataGrid<MinoColor> renderBlocks = new ObjectDataGrid<>(PANEL_WIDTH_BLOCKS, PANEL_HEIGHT_BLOCKS);
    private PlayerRenderData pdr = null;

    public void setRenderBlocks(ObjectDataGrid<MinoColor> renderBlocks) {
        this.renderBlocks = renderBlocks;
    }

    public void setPlayerRenderData(PlayerRenderData pdr) {
        this.pdr = pdr;
    }

    public PlayfieldPanel() {
        super(10, 20);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int y = 0; y < PANEL_HEIGHT_BLOCKS; y++) {
            for (int x = 0; x < PANEL_WIDTH_BLOCKS; x++) {
                MinoColor currColor = renderBlocks.getAtPos(x, y);
                if (currColor == null) {
                    continue;
                }
                paintMinoBlock(g, x, y, currColor);
            }
        }

        if (pdr == null) {
            return;
        }

        for (int y = 0; y < pdr.blocks.getHeight(); y++) {
            for (int x = 0; x < pdr.blocks.getWidth(); x++) {
                MinoColor currColor = pdr.blocks.getAtPos(x, y);
                if (currColor == null) {
                    continue;
                }
                paintMinoBlock(g, x + pdr.x, y + pdr.y, currColor);
                paintMinoBlock(g, x + pdr.x, y + pdr.shadowY, currColor, 0.3);
            }
        }
    }
}
