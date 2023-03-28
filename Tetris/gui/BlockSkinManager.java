package Tetris.gui;

import Tetris.data.BlockWithConnection;
import Tetris.data.BlockWithConnection.Dir;
import Tetris.data.XY;
import Tetris.data.mino.MinoColor;
import Tetris.settings.BlockConnectionMode;
import Tetris.settings.ReceiveSettings;
import Tetris.settings.SettingKey;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class BlockSkinManager implements ReceiveSettings {
    public enum SkinConnection {
        None(1, 1, new Dir[0]),
        Straight(4, 4, Dir.straights()),
        Diagonal(5, 11, Dir.values());

        private final int width;
        private final int height;
        private final int bitMask;

        SkinConnection(int width, int height, Dir[] bitsForMask) {
            this.width = width;
            this.height = height;

            int bitMask = 0;
            for (Dir dir : bitsForMask) {
                bitMask += dir.bitValue();
            }
            this.bitMask = bitMask;
        }

        int width() {
            return width;
        }

        int height() {
            return height;
        }

        int bitMask() {
            return bitMask;
        }
    }

    public class FileReadResult {
        public final Image image;
        public final SkinConnection connnection;
        public final boolean requestSmooth;

        public FileReadResult(Image image, SkinConnection connnection, boolean requestSmooth) {
            this.image = image;
            this.connnection = connnection;
            this.requestSmooth = requestSmooth;
        }
    }

    public class ReadResult {
        public final Image[] images;
        public final SkinConnection connection;

        public ReadResult(Image[] images, SkinConnection connection) {
            this.images = images;
            this.connection = connection;
        }
    }

    private static String[] blockSkinFolders = findBlockSkinFolders();
    private String selectedFolder;
    private BlockConnectionMode blockConnectionMode = BlockConnectionMode.All;
    private Map<MinoColor, ReadResult> readResults = new EnumMap<>(MinoColor.class);
    private static Map<Integer, XY> bwcToGridLocation = makeBwcToGridMap();

    private static Map<Integer, XY> makeBwcToGridMap() {
        HashMap<Integer, XY> map = new HashMap<>();
        map.put(getBWCValue(false, false, false, false, false, false, false, false), new XY(0, 0));
        map.put(getBWCValue(false, false, true, false, false, false, false, false), new XY(1, 0));
        map.put(getBWCValue(false, false, true, false, false, false, true, false), new XY(2, 0));
        map.put(getBWCValue(false, false, false, false, false, false, true, false), new XY(3, 0));
        map.put(getBWCValue(false, false, false, false, true, false, false, false), new XY(0, 1));
        map.put(getBWCValue(true, false, false, false, true, false, false, false), new XY(0, 2));
        map.put(getBWCValue(true, false, false, false, false, false, false, false), new XY(0, 3));
        map.put(getBWCValue(false, false, true, false, true, false, false, false), new XY(1, 1));
        map.put(getBWCValue(false, false, true, false, true, false, true, false), new XY(2, 1));
        map.put(getBWCValue(false, false, false, false, true, false, true, false), new XY(3, 1));
        map.put(getBWCValue(true, false, true, false, true, false, false, false), new XY(1, 2));
        map.put(getBWCValue(true, false, true, false, true, false, true, false), new XY(2, 2));
        map.put(getBWCValue(true, false, false, false, true, false, true, false), new XY(3, 2));
        map.put(getBWCValue(true, false, true, false, false, false, false, false), new XY(1, 3));
        map.put(getBWCValue(true, false, true, false, false, false, true, false), new XY(2, 3));
        map.put(getBWCValue(true, false, false, false, false, false, true, false), new XY(3, 3));
        map.put(getBWCValue(false, false, true, true, true, false, false, false), new XY(0, 4));
        map.put(getBWCValue(false, false, true, true, true, false, true, false), new XY(1, 4));
        map.put(getBWCValue(false, false, true, true, true, true, true, false), new XY(2, 4));
        map.put(getBWCValue(false, false, true, false, true, true, true, false), new XY(3, 4));
        map.put(getBWCValue(false, false, false, false, true, true, true, false), new XY(4, 4));
        map.put(getBWCValue(true, false, true, true, true, false, false, false), new XY(0, 5));
        map.put(getBWCValue(true, false, true, true, true, false, true, false), new XY(1, 5));
        map.put(getBWCValue(true, false, true, true, true, true, true, false), new XY(2, 5));
        map.put(getBWCValue(true, false, true, false, true, true, true, false), new XY(3, 5));
        map.put(getBWCValue(true, false, false, false, true, true, true, false), new XY(4, 5));
        map.put(getBWCValue(true, true, true, true, true, false, false, false), new XY(0, 6));
        map.put(getBWCValue(true, true, true, true, true, false, true, false), new XY(1, 6));
        map.put(getBWCValue(true, true, true, true, true, true, true, true), new XY(2, 6));
        map.put(getBWCValue(true, false, true, false, true, true, true, true), new XY(3, 6));
        map.put(getBWCValue(true, false, false, false, true, true, true, true), new XY(4, 6));
        map.put(getBWCValue(true, true, true, false, true, false, false, false), new XY(0, 7));
        map.put(getBWCValue(true, true, true, false, true, false, true, false), new XY(1, 7));
        map.put(getBWCValue(true, true, true, false, true, false, true, true), new XY(2, 7));
        map.put(getBWCValue(true, false, true, false, true, false, true, true), new XY(3, 7));
        map.put(getBWCValue(true, false, false, false, true, false, true, true), new XY(4, 7));
        map.put(getBWCValue(true, true, true, false, false, false, false, false), new XY(0, 8));
        map.put(getBWCValue(true, true, true, false, false, false, true, false), new XY(1, 8));
        map.put(getBWCValue(true, true, true, false, false, false, true, true), new XY(2, 8));
        map.put(getBWCValue(true, false, true, false, false, false, true, true), new XY(3, 8));
        map.put(getBWCValue(true, false, false, false, false, false, true, true), new XY(4, 8));
        map.put(getBWCValue(true, true, true, false, true, true, true, true), new XY(0, 9));
        map.put(getBWCValue(true, true, true, true, true, false, true, true), new XY(1, 9));
        map.put(getBWCValue(true, false, true, true, true, false, true, true), new XY(2, 9));
        map.put(getBWCValue(true, false, true, true, true, true, true, true), new XY(0, 10));
        map.put(getBWCValue(true, true, true, true, true, true, true, false), new XY(1, 10));
        map.put(getBWCValue(true, true, true, false, true, true, true, false), new XY(2, 10));
        return map;
    }

    private static int getBWCValue(boolean up, boolean upRight, boolean right, boolean downRight,
            boolean down, boolean downLeft, boolean left, boolean upLeft) {
        int out = 0;
        out += up ? Dir.Up.bitValue() : 0;
        out += upRight ? Dir.UpRight.bitValue() : 0;
        out += right ? Dir.Right.bitValue() : 0;
        out += downRight ? Dir.DownRight.bitValue() : 0;
        out += down ? Dir.Down.bitValue() : 0;
        out += downLeft ? Dir.DownLeft.bitValue() : 0;
        out += left ? Dir.Left.bitValue() : 0;
        out += upLeft ? Dir.UpLeft.bitValue() : 0;
        return out;
    }

    private void setSkinFolder(String folderName) {
        selectedFolder = folderName;
        readResults.clear();
    }

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
                .filter(v -> !(new File(v.getPath() + "/isHidden.txt").isFile()))
                .map(v -> v.getName())
                .toArray(String[]::new);
    }

    public static String[] getBlockSkinFolders() {
        return Arrays.copyOf(blockSkinFolders, blockSkinFolders.length);
    }

    public BlockSkinManager() {
        setSkinFolder("Default");
    }

    private int getIndexFromBWCValue(SkinConnection sc, int value) {
        XY gridLoc = bwcToGridLocation.get(value & sc.bitMask());
        return gridLoc.x + (gridLoc.y) * sc.width();
    }

    public Image getImage(MinoColor mc) {
        return getImage(mc, 0);
    }

    private Image getImage(MinoColor mc, int bwcValue) {
        if (!readResults.containsKey(mc)) {
            readResults.put(mc, getImagesFromFolder(selectedFolder, mc));
        }

        return readResults.get(mc).images[getIndexFromBWCValue(readResults.get(mc).connection, bwcValue)];
    }

    public Image getImage(BlockWithConnection bwc) {
        switch (blockConnectionMode) {
            case All:
                return getImage(bwc.getMinoColor(), bwc.getConnectionAll());
            case Color:
                return getImage(bwc.getMinoColor(), bwc.getConnectionColor());
            case Mino:
                return getImage(bwc.getMinoColor(), bwc.getConnectionMino());
            default:
                return getImage(bwc.getMinoColor(), 0);
        }
    }

    public Image getImage(BlockWithConnection bwc, MinoColor overrideColor) {
        switch (blockConnectionMode) {
            case All:
                return getImage(overrideColor, bwc.getConnectionAll());
            case Color:
                return getImage(overrideColor, bwc.getConnectionColor());
            case Mino:
                return getImage(overrideColor, bwc.getConnectionMino());
            default:
                return getImage(overrideColor, 0);
        }
    }

    public ReadResult getImagesFromFolder(String folderName, MinoColor mc) {
        Image loadedImage;
        SkinConnection connected = SkinConnection.None;
        boolean requestSmooth = false;
        try {
            FileReadResult result = readFromFolder(folderName, mc);
            loadedImage = result.image;
            connected = result.connnection;
            requestSmooth = result.requestSmooth;
        } catch (IOException e) {
            loadedImage = generateErrorImage(mc);
            connected = SkinConnection.None;
        }

        return new ReadResult(cutImageGrid(loadedImage, connected.width(), connected.height(), requestSmooth),
                connected);
    }

    private FileReadResult readFromFolder(String folder, MinoColor mc) throws IOException {
        Image loadedImage = ImageIO.read(new File(filepath(mc, folder)));
        SkinConnection connection = getSkinConnectionMetadata(folder);
        boolean requestSmooth = new File(folderpath(folder) + "/requestSmoothScaling.txt").isFile();
        boolean throwError = false;

        if (loadedImage.getWidth(null) % connection.width() != 0) {
            System.err.printf("%s block skin's %s width is not divisible by %s%n", folder, mc, connection.width());
            throwError = true;
        }
        if (loadedImage.getHeight(null) % connection.height() != 0) {
            System.err.printf("%s block skin's %s height is not divisible by %s%n", folder, mc, connection.height());
            throwError = true;
        }

        if (throwError) {
            throw new IOException();
        }
        return new FileReadResult(loadedImage, connection, requestSmooth);
    }

    private SkinConnection getSkinConnectionMetadata(String folder) {
        if (new File(folderpath(folder) + "/isConnectedDiagonal.txt").isFile()) {
            return SkinConnection.Diagonal;
        }
        if (new File(folderpath(folder) + "/isConnected.txt").isFile()) {
            return SkinConnection.Straight;
        }
        return SkinConnection.None;
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

    private Image[] cutImageGrid(Image loadedImage, int width, int height, boolean smoothScale) {
        Image[] currImages = new Image[width * height];
        Image scaledImage = loadedImage.getScaledInstance(MinoPanel.BLOCK_WIDTH * width,
                MinoPanel.BLOCK_HEIGHT * height, smoothScale ? Image.SCALE_SMOOTH : Image.SCALE_REPLICATE);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BufferedImage bi = new BufferedImage(MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.getGraphics();
                g.drawImage(scaledImage,
                        0, 0,
                        MinoPanel.BLOCK_WIDTH, MinoPanel.BLOCK_HEIGHT,
                        x * MinoPanel.BLOCK_WIDTH, y * MinoPanel.BLOCK_HEIGHT,
                        (x + 1) * MinoPanel.BLOCK_WIDTH, (y + 1) * MinoPanel.BLOCK_HEIGHT,
                        null);
                g.dispose();
                currImages[x + (width * y)] = bi;
            }
        }
        return currImages;
    }

    @Override
    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.BlockSkin, x -> {
            setSkinFolder((String) x);
        });
        receiversMap.put(SettingKey.BlockConnectionMode, x -> {
            blockConnectionMode = (BlockConnectionMode) x;
        });
        return receiversMap;
    }

}
