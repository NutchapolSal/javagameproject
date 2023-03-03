package Tetris;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class KickTableMap implements KickTable {
    static final XY[] noKick = new XY[] { new XY(0, 0) };
    private Map<Direction, Map<Direction, XY[]>> table;

    public XY[] getKicks(Direction beginDir, Direction destDir) {
        var output = table.get(beginDir).get(destDir);
        if (output == null) {
            output = noKick;
        }

        return Arrays.copyOf(output, output.length);
    }

    private KickTableMap(KickTableBuilder ktb) {
        this.table = ktb.table;
    }

    public static class KickTableBuilder {
        private Map<Direction, Map<Direction, XY[]>> table;

        public KickTableBuilder() {
            table = new EnumMap<>(Direction.class);
            for (Direction dir : Direction.values()) {
                table.put(dir, new EnumMap<>(Direction.class));
            }
        }

        public void setKicks(Direction beginDir, Direction destDir, XY[] kicks) {
            table.get(beginDir).put(destDir, kicks);
        }

        public KickTableMap build() {
            return new KickTableMap(this);
        }
    }
}
