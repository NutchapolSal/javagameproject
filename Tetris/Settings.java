package Tetris;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class Settings {
    private Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    private HandlingPreset handlingPreset;
    private int dasChargeFrames;
    private int autoRepeatFrames;
    private boolean sonicDrop;
    private ControlScheme controlScheme;
    private GameplayMode gameplayMode;

    private EnumMap<SettingKey, Consumer<Object>> receivers = new EnumMap<>(SettingKey.class);

    public Settings() {
        load();
    }

    public HandlingPreset getHandlingPreset() {
        return handlingPreset;
    }

    public boolean getSonicDrop() {
        return sonicDrop;
    }

    public ControlScheme getControlScheme() {
        return controlScheme;
    }

    public GameplayMode getGameplayMode() {
        return gameplayMode;
    }

    public void bindReceiver(SettingKey sk, Consumer<Object> receiver) {
        receivers.put(sk, receiver);
    }

    private void processHandlingPreset() {
        switch (handlingPreset) {
            case Default:
                dasChargeFrames = 9;
                autoRepeatFrames = 2;
                break;
            case Fast:
                dasChargeFrames = 4;
                autoRepeatFrames = 2;
                break;
            default:
                break;
        }
    }

    public void loadSettingsToReceivers() {
        receivers.get(SettingKey.DasChargeFrames).accept(this.dasChargeFrames);
        receivers.get(SettingKey.AutoRepeatFrames).accept(this.autoRepeatFrames);
        receivers.get(SettingKey.SonicDrop).accept(this.sonicDrop);
        receivers.get(SettingKey.ControlScheme).accept(this.controlScheme);
        receivers.get(SettingKey.GameplayMode).accept(this.gameplayMode);
    }

    public void setHandlingPreset(HandlingPreset handlingPreset) {
        this.handlingPreset = handlingPreset;
        processHandlingPreset();
        save(SettingKey.HandlingPreset);
        save(SettingKey.DasChargeFrames);
        save(SettingKey.AutoRepeatFrames);
        receivers.get(SettingKey.DasChargeFrames).accept(this.dasChargeFrames);
        receivers.get(SettingKey.AutoRepeatFrames).accept(this.autoRepeatFrames);
    }

    public void setDasChargeFrames(int dasChargeFrames) {
        this.dasChargeFrames = dasChargeFrames;
        save(SettingKey.DasChargeFrames);
        receivers.get(SettingKey.DasChargeFrames).accept(this.dasChargeFrames);
    }

    public void setAutoRepeatFrames(int autoRepeatFrames) {
        this.autoRepeatFrames = autoRepeatFrames;
        save(SettingKey.AutoRepeatFrames);
        receivers.get(SettingKey.AutoRepeatFrames).accept(this.autoRepeatFrames);
    }

    public void setSonicDrop(boolean sonicDrop) {
        this.sonicDrop = sonicDrop;
        save(SettingKey.SonicDrop);
        receivers.get(SettingKey.SonicDrop).accept(this.sonicDrop);
    }

    public void setControlScheme(ControlScheme controlScheme) {
        this.controlScheme = controlScheme;
        save(SettingKey.ControlScheme);
        receivers.get(SettingKey.ControlScheme).accept(this.controlScheme);
    }

    public void setGameplayMode(GameplayMode gameplayMode) {
        this.gameplayMode = gameplayMode;
        save(SettingKey.GameplayMode);
        receivers.get(SettingKey.GameplayMode).accept(this.gameplayMode);
    }

    private void save(SettingKey sk) {
        switch (sk) {
            case HandlingPreset:
                prefs.put(sk.name(), this.handlingPreset.name());
                break;
            case DasChargeFrames:
                prefs.putInt(sk.name(), this.dasChargeFrames);
                break;
            case AutoRepeatFrames:
                prefs.putInt(sk.name(), this.autoRepeatFrames);
                break;
            case SonicDrop:
                prefs.putBoolean(sk.name(), this.sonicDrop);
                break;
            case ControlScheme:
                prefs.put(sk.name(), this.controlScheme.name());
                break;
            case GameplayMode:
                prefs.put(sk.name(), this.gameplayMode.name());
                break;
        }
    }

    private void load() {
        handlingPreset = HandlingPreset
                .valueOf(prefs.get(SettingKey.HandlingPreset.name(), HandlingPreset.Default.name()));
        dasChargeFrames = prefs.getInt(SettingKey.DasChargeFrames.name(), 9);
        autoRepeatFrames = prefs.getInt(SettingKey.AutoRepeatFrames.name(), 2);
        sonicDrop = prefs.getBoolean(SettingKey.SonicDrop.name(), false);
        controlScheme = ControlScheme.valueOf(prefs.get(SettingKey.ControlScheme.name(), ControlScheme.WASD.name()));
        gameplayMode = GameplayMode.valueOf(prefs.get(SettingKey.GameplayMode.name(), GameplayMode.Marathon.name()));

        processHandlingPreset();
    }

}
