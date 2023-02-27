package Tetris;

public interface RawInputSource {
    public boolean getLeft();

    public boolean getRight();

    public boolean getHold();

    public boolean getRotateCW();

    public boolean getRotateCCW();

    public boolean getRotateFlip();

    public boolean getHardDrop();

    public boolean getSoftDrop();

    public void update();
}
