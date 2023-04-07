package Tetris.gameplay;

public class ClearLinesResult {
    final int[] lineIndices;
    final int count;

    public ClearLinesResult(int[] lineIndices) {
        this.lineIndices = lineIndices;
        this.count = lineIndices.length;
    }

}
