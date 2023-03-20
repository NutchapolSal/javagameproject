package Tetris;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;

public enum MinoColor implements ReceiveSettings {
    Blue("blue"),
    Cyan("cyan"),
    Gray("gray"),
    Green("green"),
    Orange("orange"),
    Purple("purple"),
    Red("red"),
    White("white"),
    Yellow("yellow");

    private static String folderName = "Default";
    private static String[] blockSkinFolders = findBlockSkinFolders();

    private final String filename;
    private Image image;

    MinoColor(String filename) {
        this.filename = filename;
    }

    String filepath() {
        return "Tetris/blockImg/" + folderName + "/" + filename + ".png";
    }

    String filepath(String blockSkinFolder) {
        return "Tetris/blockImg/" + blockSkinFolder + "/" + filename + ".png";
    }

    Image image() {
        if (image != null) {
            return image;
        }

        image = image(folderName);
        return image;
    }

    Image image(String folderName) {
        Image currImage;
        try {
            currImage = ImageIO.read(new File(filepath(folderName))).getScaledInstance(MinoPanel.BLOCK_WIDTH,
                    MinoPanel.BLOCK_HEIGHT, Image.SCALE_REPLICATE);
        } catch (IOException e) {
            BufferedImage bi = new BufferedImage(MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString(filename, 0, MinoPanel.BLOCK_HEIGHT - 5);
            g.dispose();
            currImage = bi;
        }
        return currImage;
    }

    private static Map<SettingKey, Consumer<Object>> getRealReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.BlockSkin, x -> {
            folderName = (String) x;
            for (MinoColor mc : values()) {
                mc.image = null;
            }
        });
        return receiversMap;
    }

    public Map<SettingKey, Consumer<Object>> getReceivers() {
        return getRealReceivers();
    }

    private static String[] findBlockSkinFolders() {
        return Stream.of(new File("Tetris/blockImg").listFiles(v -> v.isDirectory()))
                .map(v -> v.getName())
                .toArray(String[]::new);
    }

    public static String[] getBlockSkinFolders() {
        return Arrays.copyOf(blockSkinFolders, blockSkinFolders.length);
    }
}
