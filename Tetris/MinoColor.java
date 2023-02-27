package Tetris;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
    private Image image;

    MinoColor(String filename) {
        this.filename = "Tetris/blockImg/" + filename + ".png";
    }

    String filename() {
        return this.filename;
    }

    Image image() throws IOException {
        if (image != null) {
            return image;
        }

        image = ImageIO.read(new File(filename()));
        return image;
    }
}
