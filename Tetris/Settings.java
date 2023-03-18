package Tetris;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class Settings {
    private Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    private EnumMap<SettingKey, Consumer<Object>> receivers = new EnumMap<>(SettingKey.class);

    public Settings() {
        processHandlingPreset();
    }

    public HandlingPreset getHandlingPreset() {
        return HandlingPreset
                .valueOf(prefs.get(SettingKey.HandlingPreset.name(), HandlingPreset.Default.name()));
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

    private void saveHandlingPreset(HandlingPreset in) {
        prefs.put(SettingKey.HandlingPreset.name(), in.name());
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

    private void processHandlingPreset() {
        switch (getHandlingPreset()) {
            case Default:
                saveDasChargeFrames(9);
                saveAutoRepeatFrames(2);
                break;
            case Fast:
                saveDasChargeFrames(4);
                saveAutoRepeatFrames(2);
                break;
            default:
                break;
        }
    }

    public void loadSettingsToReceivers() {
        receivers.get(SettingKey.DasChargeFrames).accept(getDasChargeFrames());
        receivers.get(SettingKey.AutoRepeatFrames).accept(getAutoRepeatFrames());
        receivers.get(SettingKey.SonicDrop).accept(getSonicDrop());
        receivers.get(SettingKey.ControlScheme).accept(getControlScheme());
        receivers.get(SettingKey.GameplayMode).accept(getGameplayMode());
    }

    public void setHandlingPreset(HandlingPreset handlingPreset) {
        saveHandlingPreset(handlingPreset);
        processHandlingPreset();
        receivers.get(SettingKey.DasChargeFrames).accept(getDasChargeFrames());
        receivers.get(SettingKey.AutoRepeatFrames).accept(getAutoRepeatFrames());
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
