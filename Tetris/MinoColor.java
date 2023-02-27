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
        this.filename = "Tetris/blockImg/" + filename + ".png";
    }

    String filename() {
        return this.filename;
    }

    Image image() {
        if (image != null) {
            return image;
        }

        try {
            image = ImageIO.read(new File(filename()));
        } catch (IOException e) {
            e.printStackTrace();
            BufferedImage bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 20, 20);
            g.setColor(Color.WHITE);
            g.drawString("Err", 0, 20);
            g.dispose();
            image = bi;
        }
        return image;
    }

}
