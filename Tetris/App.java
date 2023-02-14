package Tetris;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class App {
    private JFrame f;
    private JLabel levelLabel;
    private JLabel linesLabel;
    private JLabel timeLabel;
    private JLabel levelCountLabel;
    private JLabel linesCountLabel;
    private JLabel timeCountLabel;
    private JPanel playfield;
    private JPanel hold;
    private JPanel next;
    private JButton newGame;

    public App() {
        f = new JFrame("Tetris");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        detailComponents();
        f.setVisible(true);
    }

    private void detailComponents() {
        levelLabel = new JLabel();
        linesLabel = new JLabel();
        timeLabel = new JLabel();
        levelCountLabel = new JLabel();
        linesCountLabel = new JLabel();
        timeCountLabel = new JLabel();
        playfield = new JPanel();
        hold = new JPanel();
        next = new JPanel();
        newGame = new JButton();
    }
}
