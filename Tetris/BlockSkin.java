package Tetris;

public enum BlockSkin {
    First("first"), Second("second");

    private String folderName;

    String folderName() {
        return folderName;
    }

    BlockSkin(String folderName) {
        this.folderName = folderName;
    }
}
