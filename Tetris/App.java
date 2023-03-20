package Tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    public App() {
        Settings settings = new Settings();
        Gui gui = new Gui();
        gui.bindToSettings(settings);
        Gameplay gameplay = new Gameplay();
        gameplay.setRawInputSource(gui.getKeyboardHandler());

        settings.bindReceiver(SettingKey.DasChargeFrames, gameplay.getDASReceiver());
        settings.bindReceiver(SettingKey.AutoRepeatFrames, gameplay.getARRReceiver());
        settings.bindReceiver(SettingKey.SonicDrop, gameplay.getSonicDropReceiver());
        settings.bindReceiver(SettingKey.SonicDrop, gui.getSonicDropReceiver());
        settings.bindReceiver(SettingKey.ControlScheme, gui.getControlSchemeReceiver());
        settings.bindReceiver(SettingKey.ControlScheme, gui.getKeyboardHandler().getControlSchemeReceiver());
        settings.bindReceiver(SettingKey.GameplayMode, gameplay.getGameplayModeReceiver());
        settings.bindReceiver(SettingKey.BlockSkin, MinoColor.getBlockSkinReceiver());
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
    }
}
