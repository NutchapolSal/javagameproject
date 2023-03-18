package Tetris;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class Settings {
    private Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    private HandlingPreset handlingPreset;
    private EnumMap<SettingKey, Consumer<Object>> receivers = new EnumMap<>(SettingKey.class);

    public Settings() {
        interpretHandlingPreset();
    }

    public HandlingPreset getHandlingPreset() {
        return handlingPreset;
    }

    public boolean getSonicDrop() {
        return prefs.getBoolean(SettingKey.SonicDrop.name(), false);
    }

    public int getDasChargeFrames() {
        return prefs.getInt(SettingKey.DasChargeFrames.name(), 9);
    }

    public int getAutoRepeatFrames() {
        return prefs.getInt(SettingKey.AutoRepeatFrames.name(), 2);
    }

    public ControlScheme getControlScheme() {
        return ControlScheme.valueOf(prefs.get(SettingKey.ControlScheme.name(), ControlScheme.WASD.name()));
    }

    public GameplayMode getGameplayMode() {
        return GameplayMode.valueOf(prefs.get(SettingKey.GameplayMode.name(), GameplayMode.Marathon.name()));
    }

    private void saveDasChargeFrames(int in) {
        prefs.putInt(SettingKey.DasChargeFrames.name(), in);
    }

    private void saveAutoRepeatFrames(int in) {
        prefs.putInt(SettingKey.AutoRepeatFrames.name(), in);
    }

    private void saveSonicDrop(Boolean in) {
        prefs.putBoolean(SettingKey.SonicDrop.name(), in);
    }

    private void saveControlScheme(ControlScheme in) {
        prefs.put(SettingKey.ControlScheme.name(), in.name());
    }

    private void saveGameplayMode(GameplayMode in) {
        prefs.put(SettingKey.GameplayMode.name(), in.name());
    }

    public void bindReceiver(SettingKey sk, Consumer<Object> receiver) {
        receivers.put(sk, receiver);
    }

    private void interpretHandlingPreset() {
        if (getDasChargeFrames() == 9 && getAutoRepeatFrames() == 2) {
            handlingPreset = HandlingPreset.Default;
            return;
        }
        if (getDasChargeFrames() == 4 && getAutoRepeatFrames() == 2) {
            handlingPreset = HandlingPreset.Fast;
            return;
        }

        handlingPreset = HandlingPreset.Custom;
    }

    public void loadSettingsToReceivers() {
        receivers.get(SettingKey.DasChargeFrames).accept(getDasChargeFrames());
        receivers.get(SettingKey.AutoRepeatFrames).accept(getAutoRepeatFrames());
        receivers.get(SettingKey.SonicDrop).accept(getSonicDrop());
        receivers.get(SettingKey.ControlScheme).accept(getControlScheme());
        receivers.get(SettingKey.GameplayMode).accept(getGameplayMode());
    }

    public void setHandlingPreset(HandlingPreset handlingPreset) {
        switch (handlingPreset) {
            case Default:
                setDasChargeFrames(9);
                setAutoRepeatFrames(2);
                break;
            case Fast:
                setDasChargeFrames(4);
                setAutoRepeatFrames(2);
                break;
            default:
                break;
        }
        this.handlingPreset = handlingPreset;
    }

    public void setDasChargeFrames(int dasChargeFrames) {
        saveDasChargeFrames(dasChargeFrames);
        receivers.get(SettingKey.DasChargeFrames).accept(getDasChargeFrames());
    }

    public void setAutoRepeatFrames(int autoRepeatFrames) {
        saveAutoRepeatFrames(autoRepeatFrames);
        receivers.get(SettingKey.AutoRepeatFrames).accept(getAutoRepeatFrames());
    }

    public void setSonicDrop(boolean sonicDrop) {
        saveSonicDrop(sonicDrop);
        receivers.get(SettingKey.SonicDrop).accept(getSonicDrop());
    }

    public void setControlScheme(ControlScheme controlScheme) {
        saveControlScheme(controlScheme);
        receivers.get(SettingKey.ControlScheme).accept(getControlScheme());
    }

    public void setGameplayMode(GameplayMode gameplayMode) {
        saveGameplayMode(gameplayMode);
        receivers.get(SettingKey.GameplayMode).accept(getGameplayMode());
    }
}
