package Tetris;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;

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
        this.filename = filename;
    }

    String filepath() {
        return "Tetris/blockImg/" + filename + ".png";
    }

    Image image() {
        if (image != null) {
            return image;
        }

        try {
            image = ImageIO.read(new File(filepath()));
        } catch (IOException e) {
            BufferedImage bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 20, 20);
            g.setColor(Color.WHITE);
            g.drawString(filename, 0, 15);
            g.dispose();
            image = bi;
        }
        return image;
    }

}
