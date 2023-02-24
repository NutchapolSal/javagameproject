package Tetris;

import java.util.Timer;
import java.util.TimerTask;

public class App {

    public App() {
        Gui g = new Gui();

        testkbhandler(g);
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
