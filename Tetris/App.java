package Tetris;

import Tetris.gui.SwingTetrisGui;
import Tetris.settings.Settings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    public App() {
        Settings settings = new Settings();
        SwingTetrisGui gui = new SwingTetrisGui();
        gui.bindToSettings(settings);
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(gui.getKeyboardHandler());

        settings.bindReceivers(gameplay);
        settings.bindReceivers(gui);
        settings.bindReceivers(gui.getKeyboardHandler());
        settings.bindReceivers(gui.getBlockSkinManager());
        settings.loadSettingsToReceivers();

        ActionListener guiUpdater = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.update(gameplay.getGuiData());
            }
        };

        ActionListener newGameAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameplay.startGame();
            }
        };

        gui.setNewGameAction(newGameAction);
        new javax.swing.Timer(8, guiUpdater).start();

        // BlockSkinImages.get().getImageFromFolder("Pixel Connected", MinoColor.Blue);
    }
}
