package Tetris.gameplay;

public class ClearLinesResult {
    public final int[] lineIndices;
    public final int count;

    public ClearLinesResult(int[] lineIndices) {
        this.lineIndices = lineIndices;
        this.count = lineIndices.length;
    }

}
