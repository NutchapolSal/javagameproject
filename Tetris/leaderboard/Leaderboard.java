package Tetris.leaderboard;

import Tetris.settings.GameplayMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Leaderboard {
    public static class LeaderboardEntry {
        public final String id;
        public final Date date;
        public final String nameP1;
        public final String nameP2;

        public final GameplayMode gameplayMode;
        public final int lines;
        public final long time;
        public final int score;

        public LeaderboardEntry(String id, Date date, String nameP1, String nameP2, GameplayMode gameplayMode,
                int lines, long time, int score) {
            this.id = id;
            this.date = date;
            this.nameP1 = nameP1;
            this.nameP2 = nameP2;
            this.gameplayMode = gameplayMode;
            this.lines = lines;
            this.time = time;
            this.score = score;
        }

        @Override
        public String toString() {
            return "LeaderboardEntry [id=" + id + ", date=" + date + ", nameP1=" + nameP1 + ", nameP2=" + nameP2
                    + ", gameplayMode=" + gameplayMode + ", lines=" + lines + ", time=" + time + ", score=" + score
                    + "]";
        }

    }

    private List<LeaderboardEntry> records = new ArrayList<>();

    private Preferences prefs = Preferences.userNodeForPackage(Leaderboard.class);

    public Leaderboard() {
        try {
            for (String id : prefs.childrenNames()) {
                records.add(getEntry(id));
            }
        } catch (BackingStoreException e) {
            System.err.println(e);
        }
    }

    private LeaderboardEntry getEntry(String id) {
        Preferences entryNode = prefs.node(id);

        return new LeaderboardEntry(
                id,
                new Date(entryNode.getLong("date", 0)),
                entryNode.get("nameP1", "?"),
                entryNode.get("nameP2", "?"),
                GameplayMode.valueOf(entryNode.get("gamemode", GameplayMode.Marathon.name())),
                entryNode.getInt("lines", -1),
                entryNode.getLong("time", -1),
                entryNode.getInt("score", -1));
    }

    public void addEntry(Date date, String nameP1, String nameP2, GameplayMode gameplayMode,
            int lines, long time, int score) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        Preferences entryNode = prefs.node(id);
        entryNode.put("nameP1", nameP1);
        entryNode.put("nameP2", nameP2);
        entryNode.putLong("date", date.getTime());
        entryNode.put("gamemode", gameplayMode.name());
        entryNode.putInt("lines", lines);
        entryNode.putLong("time", time);
        entryNode.putInt("score", score);

        records.add(getEntry(id));
    }

    public void removeEntry(String id) {
        Preferences entryNode = prefs.node(id);
        try {
            entryNode.removeNode();
            records.removeIf(le -> le.id.equals(id));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
