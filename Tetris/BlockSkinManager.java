package Tetris;

import java.awt.Image;
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

public class BlockSkinManager implements ReceiveSettings {
    private static String[] blockSkinFolders = findBlockSkinFolders();
    private String selectedFolder = "Pixel Connected";
    private Map<MinoColor, Image[]> images = new EnumMap<>(MinoColor.class);

    private String filepath(MinoColor mc) {
        return filepath(mc, selectedFolder);
    }

    private String filepath(MinoColor mc, String blockSkinFolder) {
        return folderpath(blockSkinFolder) + "/" + mc.filename() + ".png";
    }

    private String folderpath(String blockSkinFolder) {
        return "Tetris/blockImg/" + blockSkinFolder;
    }

    private static String[] findBlockSkinFolders() {
        return Stream.of(new File("Tetris/blockImg").listFiles(v -> v.isDirectory()))
                .map(v -> v.getName())
                .toArray(String[]::new);
    }

    public static String[] getBlockSkinFolders() {
        return Arrays.copyOf(blockSkinFolders, blockSkinFolders.length);
    }

    public BlockSkinManager() {
    }

    private static int getIndexFromConnetions(boolean up, boolean right, boolean down, boolean left) {
        int x = right ? 2 : 1;
        int y = down ? 2 : 1;
        if (left) {
            x = -x + 5;
        }
        if (up) {
            y = -y + 5;
        }
        return (x - 1) + ((y - 1) * 4);
    }

    public Image getImage(MinoColor mc) {
        return getImage(mc, false, false, false, false);
    }

    public Image getImage(MinoColor mc, boolean up, boolean right, boolean down, boolean left) {
        if (images.containsKey(mc)) {
            return images.get(mc)[getIndexFromConnetions(up, right, down, left)];
        }

        images.put(mc, getImageFromFolder(selectedFolder, mc));

        return images.get(mc)[getIndexFromConnetions(up, right, down, left)];
    }

    public Image[] getImageFromFolder(String folderName, MinoColor mc) {
        Image loadedImage;
        boolean connected = true;
        try {
            loadedImage = ImageIO.read(new File(filepath(mc, selectedFolder)));
            boolean connectedMetadata = new File(folderpath(folderName) + "/isConnected.txt").isFile();
            if (connectedMetadata && loadedImage.getWidth(null) % 4 != 0) {
                connected = false;
                System.err.printf("%s block skin's %s width is not divisible by 4!%n", folderName, mc);
            }
            if (connectedMetadata && loadedImage.getHeight(null) % 4 != 0) {
                connected = false;
                System.err.printf("%s block skin's %s height is not divisible by 4!%n", folderName, mc);
            }
            connected = connected && connectedMetadata;

        } catch (IOException e) {
            BufferedImage bi = new BufferedImage(MinoPanel.BLOCK_WIDTH,
                    MinoPanel.BLOCK_HEIGHT,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString(mc.filename(), 0, MinoPanel.BLOCK_HEIGHT - 5);
            g.dispose();
            loadedImage = bi;
            connected = false;
        }

        Image[] currImages = new Image[16];
        if (!connected) {
            Image scaledImage = loadedImage.getScaledInstance(MinoPanel.BLOCK_WIDTH,
                    MinoPanel.BLOCK_HEIGHT, Image.SCALE_REPLICATE);
            for (int i = 0; i < currImages.length; i++) {
                currImages[i] = scaledImage;
            }
            return currImages;
        }

        Image scaledImage = loadedImage.getScaledInstance(MinoPanel.BLOCK_WIDTH * 4,
                MinoPanel.BLOCK_HEIGHT * 4, Image.SCALE_REPLICATE);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BufferedImage bi = new BufferedImage(MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(scaledImage,
                        0, 0,
                        MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT,
                        x * MinoPanel.BLOCK_WIDTH, y * MinoPanel.BLOCK_HEIGHT,
                        (x + 1) * MinoPanel.BLOCK_WIDTH, (y + 1) * MinoPanel.BLOCK_HEIGHT,
                        null);
                g.dispose();
                currImages[x + (4 * y)] = bi;
            }
        }

        return currImages;
    }

    @Override
    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.BlockSkin, x -> {
            selectedFolder = (String) x;
            images.clear();
        });
        return receiversMap;
    }

}
