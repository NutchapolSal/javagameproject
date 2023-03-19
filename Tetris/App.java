package Tetris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
        settings.bindReceiver(SettingKey.GameplayMode, gameplay.getGameplayModeReceiver());
        settings.loadSettingsToReceivers();

        gameplay.startGame();

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
