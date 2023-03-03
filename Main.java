import javax.swing.SwingUtilities;

import Tetris.App;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}