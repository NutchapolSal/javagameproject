package Tetris;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
    private int seconds;
    private Timer timer;

    public Countdown(int seconds) {
        this.seconds = seconds;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(seconds / 60 + ":" + String.format("%02d", seconds % 60));
                seconds--;
                if (seconds < 0) {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public static void main(String[] args) {
        Countdown countdown = new Countdown(120);
        countdown.start();
    }
}