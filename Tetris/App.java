package Tetris;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App {

    public App() {
        Gui gui = new Gui();
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(gui.getKeyboardHandler());

        // gameplay.startGame();

        // testkbhandler(g);

        ActionListener guiUpdater = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.update(gameplay.getGuiDataSource());
            }
        };
        new javax.swing.Timer(16, guiUpdater).start();
    }

    private void testkbhandler(Gui g) {
        var kb = g.getKeyboardHandler();
        var pi = new PlayerInput(kb);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pi.tick();
            }
        }, 0, 100);
    }

}
