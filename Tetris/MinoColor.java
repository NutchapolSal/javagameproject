package Tetris;

public enum MinoColor {
    Blue("blue"),
    Cyan("cyan"),
    Gray("gray"),
    Green("green"),
    Orange("orange"),
    Purple("purple"),
    Red("red"),
    White("white"),
    Yellow("yellow");

    private final String filename;

    MinoColor(String filename) {
        this.filename = "Tetris/blockImg/" + filename + ".png";
    }

    String filename() {
        return this.filename;
    }
}
