package Tetris;

import java.awt.Color;
import java.awt.Graphics;
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

public class BlockSkinManager implements ReceiveSettings {
    enum SkinConnection {
        None, Straight, Diagonal
    }

    class ReadResult {
        public final Image image;
        public final SkinConnection connnection;

        public ReadResult(Image image, SkinConnection connnection) {
            this.image = image;
            this.connnection = connnection;
        }
    }

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
        SkinConnection connected = SkinConnection.None;
        try {
            ReadResult result = readFromFolder(folderName, mc);
            loadedImage = result.image;
            connected = result.connnection;
        } catch (IOException e) {
            loadedImage = generateErrorImage(mc);
            connected = SkinConnection.None;
        }

        Image[] currImages = new Image[55];
        if (connected == SkinConnection.None) {
            Image scaledImage = loadedImage.getScaledInstance(MinoPanel.BLOCK_WIDTH,
                    MinoPanel.BLOCK_HEIGHT, Image.SCALE_REPLICATE);
            for (int i = 0; i < currImages.length; i++) {
                currImages[i] = scaledImage;
            }
            return currImages;
        }

        return cutImageGrid(loadedImage, 4, 4);
    }

    private ReadResult readFromFolder(String folder, MinoColor mc) throws IOException {
        Image loadedImage = ImageIO.read(new File(filepath(mc, folder)));
        SkinConnection connection = SkinConnection.None;
        boolean file2Found = new File(folderpath(folder) + "/isConnectedDiagonal.txt").isFile();
        boolean fileFound = new File(folderpath(folder) + "/isConnected.txt").isFile();
        boolean throwError = false;
        if (file2Found) {
            connection = SkinConnection.Diagonal;
            if (loadedImage.getWidth(null) % 5 != 0) {
                System.err.printf("%s block skin's %s width is not divisible by 5%n", folder, mc);
                throwError = true;
            }
            if (loadedImage.getHeight(null) % 11 != 0) {
                System.err.printf("%s block skin's %s height is not divisible by 11%n", folder, mc);
                throwError = true;
            }
        } else if (fileFound) {
            connection = SkinConnection.Straight;
            if (loadedImage.getWidth(null) % 4 != 0) {
                System.err.printf("%s block skin's %s width is not divisible by 4%n", folder, mc);
                throwError = true;
            }
            if (loadedImage.getHeight(null) % 4 != 0) {
                System.err.printf("%s block skin's %s height is not divisible by 4%n", folder, mc);
                throwError = true;
            }
        }
        if (throwError) {
            throw new IOException();
        }
        return new ReadResult(loadedImage, connection);
    }

    private Image generateErrorImage(MinoColor mc) {
        BufferedImage bi = new BufferedImage(MinoPanel.BLOCK_WIDTH,
                MinoPanel.BLOCK_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString(mc.filename(), 0, MinoPanel.BLOCK_HEIGHT - 5);
        g.dispose();
        return bi;
    }

    private Image[] cutImageGrid(Image loadedImage, int width, int height) {
        Image[] currImages = new Image[width * height];
        Image scaledImage = loadedImage.getScaledInstance(MinoPanel.BLOCK_WIDTH * width,
                MinoPanel.BLOCK_HEIGHT * height, Image.SCALE_REPLICATE);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
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
                currImages[x + (height * y)] = bi;
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
