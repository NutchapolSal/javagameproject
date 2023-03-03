package Tetris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App {

    public App() {
        Gui gui = new Gui();
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(gui.getKeyboardHandler());

        gameplay.startGame();

        // testkbhandler(g);

        ActionListener guiUpdater = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.update(gameplay.getGuiData());
            }
        };

        ActionListener newGameAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameplay.startGame();
            }
        };

        gui.setNewGameAction(newGameAction);
        new javax.swing.Timer(8, guiUpdater).start();
    }
}
