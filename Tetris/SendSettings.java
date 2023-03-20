package Tetris;

public interface SendSettings {
    /**
     * implementer will call Settings.set*(...) to send data to Settings and may
     * call Settings.get*() to correct self's state
     */
    public void bindToSettings(Settings s);
}
