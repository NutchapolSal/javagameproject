package Tetris;

import java.awt.Graphics;
import javax.swing.BorderFactory;
import java.awt.Color;

public class PlayfieldPanel extends MinoPanel {

    public PlayfieldPanel() {
        super(10, 20);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int y = 0; y < PANEL_HEIGHT_BLOCKS; y++) {
            for (int x = 0; x < PANEL_WIDTH_BLOCKS; x++) {
                paintMinoBlock(g, x, y, MinoColor.White);
            }
        }
    }
}
