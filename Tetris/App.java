package Tetris;

import javax.swing.JFrame;

public class App {
    private JFrame f;

    public App() {
        f = new JFrame("Tetris");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ...
        f.setVisible(true);
    }
}
