package Tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    public App() {
        BlockSkinManager blockSkinManager = new BlockSkinManager();
        Settings settings = new Settings();
        SwingTetrisGui gui = new SwingTetrisGui();
        gui.bindToSettings(settings);
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(gui.getKeyboardHandler());

        settings.bindReceivers(gameplay);
        settings.bindReceivers(gui);
        settings.bindReceivers(gui.getKeyboardHandler());
        settings.bindReceivers(MinoColor.Blue);
        settings.bindReceivers(blockSkinManager);
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

        BlockSkinImages.get().getImageFromFolder("Pixel Connected", MinoColor.Blue);
    }
}
