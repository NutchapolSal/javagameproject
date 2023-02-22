package Tetris;

import java.util.Map;

public class KickTable {
    private Map<Direction, Map<Direction, XY>> table;
  
    public XY getKick(Direction beginDir, Direction destDir){
        return table.get(beginDir).get(destDir);
    }
    private KickTable(KickTableBuilder ktb) {
      this.table = ktb.table;
    }
    
    public static class KickTableBuilder {
      private Map<Direction, Map<Direction, XY>> table;
      public KickTableBuilder() {
        ...
      }
      public void setKick(Direction beginDir,Direction destDir,XY kick) {
        ...
      }
      public KickTable build() {
        return new KickTable(this);
      }
    }
  }
