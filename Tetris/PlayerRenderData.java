package Tetris;

public class PlayerRenderData {
    public final ObjectDataGrid<MinoColor> blocks;
    public final int x;
    public final int y;
    public final int shadowY;

    public PlayerRenderData(ObjectDataGrid<MinoColor> blocks, int x, int y, int shadowY) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
        this.shadowY = shadowY;
    }

}
