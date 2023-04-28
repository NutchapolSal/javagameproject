package Tetris;

import Tetris.gameplay.Gameplay;
import Tetris.gui.SwingTetrisGui;
import Tetris.leaderboard.Leaderboard;
import Tetris.settings.Settings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class App {

    public App() {
        Leaderboard leaderboard = new Leaderboard();
        Settings settings = new Settings();
        SwingTetrisGui gui = new SwingTetrisGui();
        gui.bindToSettings(settings);
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(0, gui.getKeyboardHandler(0));
        gameplay.setRawInputSource(1, gui.getKeyboardHandler(1));

        settings.bindReceivers(gameplay);
        settings.bindReceivers(gui);
        settings.bindReceivers(gui.getKeyboardHandler(0));
        settings.bindReceivers(gui.getKeyboardHandler(1));
        settings.bindReceivers(gui.getBlockSkinManager());
        settings.bindReceivers(gui.getQuickSettings());
        settings.bindReceivers(gui.getOptionsMenuGroup());
        settings.loadSettingsToReceivers();

        ActionListener newGameAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameplay.startGame();
            }
        };

        gui.setNewGameAction(newGameAction);

        ActionListener submitToLeaderboard = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var a = gui.getQuickSettings().getLeaderboardData();
                var b = gameplay.getLeaderboardData();
                leaderboard.addEntry(b.date, a.nameP1, a.nameP2, b.gameplayMode,
                        b.lines, b.time, b.score);
            }
        };
        gameplay.setSubmitLeaderboardAction(submitToLeaderboard);

        ActionListener guiUpdater = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.update(gameplay.getGuiData());
            }
        };
        new Timer(4, guiUpdater).start();
    }
}
