import javax.swing.SwingUtilities;

import Tetris.Gui;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui a = new Gui();
        });
    }
}