package Tetris.settings;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class Settings {
    private Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    private HandlingPreset handlingPreset;
    private HandlingPreset handlingPresetP2;
    private EnumMap<SettingKey, List<Consumer<Object>>> receivers = new EnumMap<>(SettingKey.class);

    public Settings() {
        interpretHandlingPreset();
        interpretHandlingPresetP2();
    }

    public HandlingPreset getHandlingPreset() {
        return handlingPreset;
    }

    public HandlingPreset getHandlingPresetP2() {
        return handlingPresetP2;
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

    public boolean getSonicDropP2() {
        return prefs.getBoolean(SettingKey.SonicDropP2.name(), false);
    }

    public int getDasChargeFramesP2() {
        return prefs.getInt(SettingKey.DasChargeFramesP2.name(), 9);
    }

    public int getAutoRepeatFramesP2() {
        return prefs.getInt(SettingKey.AutoRepeatFramesP2.name(), 2);
    }

    public ControlScheme getControlSchemeP2() {
        return ControlScheme.valueOf(prefs.get(SettingKey.ControlSchemeP2.name(), ControlScheme.WASD.name()));
    }

    public GameplayMode getGameplayMode() {
        return GameplayMode.valueOf(prefs.get(SettingKey.GameplayMode.name(), GameplayMode.Marathon.name()));
    }

    public String getBlockSkin() {
        return prefs.get(SettingKey.BlockSkin.name(), "Default");
    }

    public BlockConnectionMode getBlockConnectionMode() {
        return BlockConnectionMode
                .valueOf(prefs.get(SettingKey.BlockConnectionMode.name(), BlockConnectionMode.None.name()));
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

    private void saveDasChargeFramesP2(int in) {
        prefs.putInt(SettingKey.DasChargeFramesP2.name(), in);
    }

    private void saveAutoRepeatFramesP2(int in) {
        prefs.putInt(SettingKey.AutoRepeatFramesP2.name(), in);
    }

    private void saveSonicDropP2(Boolean in) {
        prefs.putBoolean(SettingKey.SonicDropP2.name(), in);
    }

    private void saveControlSchemeP2(ControlScheme in) {
        prefs.put(SettingKey.ControlSchemeP2.name(), in.name());
    }

    private void saveGameplayMode(GameplayMode in) {
        prefs.put(SettingKey.GameplayMode.name(), in.name());
    }

    private void saveBlockSkin(String in) {
        prefs.put(SettingKey.BlockSkin.name(), in);
    }

    private void saveBlockConnectionMode(BlockConnectionMode in) {
        prefs.put(SettingKey.BlockConnectionMode.name(), in.name());
    }

    public void bindReceiver(SettingKey sk, Consumer<Object> receiver) {
        receivers.putIfAbsent(sk, new ArrayList<>());
        receivers.get(sk).add(receiver);
    }

    public void bindReceivers(ReceiveSettings rs) {
        for (var entry : rs.getReceivers().entrySet()) {
            receivers.putIfAbsent(entry.getKey(), new ArrayList<>());
            receivers.get(entry.getKey()).add(entry.getValue());
        }
    }

    public void clearReceivers(SettingKey sk) {
        receivers.putIfAbsent(sk, new ArrayList<>());
        receivers.get(sk).clear();
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

    private void interpretHandlingPresetP2() {
        if (getDasChargeFramesP2() == 9 && getAutoRepeatFramesP2() == 2) {
            handlingPresetP2 = HandlingPreset.Default;
            return;
        }
        if (getDasChargeFramesP2() == 4 && getAutoRepeatFramesP2() == 2) {
            handlingPresetP2 = HandlingPreset.Fast;
            return;
        }

        handlingPresetP2 = HandlingPreset.Custom;
    }

    public void loadSettingsToReceivers() {
        iterateOverReceivers(SettingKey.DasChargeFrames, getDasChargeFrames());
        iterateOverReceivers(SettingKey.AutoRepeatFrames, getAutoRepeatFrames());
        iterateOverReceivers(SettingKey.SonicDrop, getSonicDrop());
        iterateOverReceivers(SettingKey.ControlScheme, getControlScheme());
        iterateOverReceivers(SettingKey.HandlingPreset, getHandlingPreset());
        iterateOverReceivers(SettingKey.DasChargeFramesP2, getDasChargeFramesP2());
        iterateOverReceivers(SettingKey.AutoRepeatFramesP2, getAutoRepeatFramesP2());
        iterateOverReceivers(SettingKey.SonicDropP2, getSonicDropP2());
        iterateOverReceivers(SettingKey.ControlSchemeP2, getControlSchemeP2());
        iterateOverReceivers(SettingKey.HandlingPresetP2, getHandlingPresetP2());
        iterateOverReceivers(SettingKey.GameplayMode, getGameplayMode());
        iterateOverReceivers(SettingKey.BlockSkin, getBlockSkin());
        iterateOverReceivers(SettingKey.BlockConnectionMode, getBlockConnectionMode());
    }

    private void iterateOverReceivers(SettingKey sk, Object input) {
        for (var v : receivers.get(sk)) {
            v.accept(input);
        }
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
        iterateOverReceivers(SettingKey.HandlingPreset, handlingPreset);
    }

    public void setHandlingPresetP2(HandlingPreset handlingPreset) {
        switch (handlingPreset) {
            case Default:
                setDasChargeFramesP2(9);
                setAutoRepeatFramesP2(2);
                break;
            case Fast:
                setDasChargeFramesP2(4);
                setAutoRepeatFramesP2(2);
                break;
            default:
                break;
        }
        this.handlingPresetP2 = handlingPreset;
        iterateOverReceivers(SettingKey.HandlingPresetP2, handlingPreset);
    }

    public void setDasChargeFrames(int dasChargeFrames) {
        saveDasChargeFrames(dasChargeFrames);
        iterateOverReceivers(SettingKey.DasChargeFrames, getDasChargeFrames());
    }

    public void setAutoRepeatFrames(int autoRepeatFrames) {
        saveAutoRepeatFrames(autoRepeatFrames);
        iterateOverReceivers(SettingKey.AutoRepeatFrames, getAutoRepeatFrames());
    }

    public void setSonicDrop(boolean sonicDrop) {
        saveSonicDrop(sonicDrop);
        iterateOverReceivers(SettingKey.SonicDrop, getSonicDrop());
    }

    public void setControlScheme(ControlScheme controlScheme) {
        saveControlScheme(controlScheme);
        iterateOverReceivers(SettingKey.ControlScheme, getControlScheme());
    }

    public void setDasChargeFramesP2(int dasChargeFrames) {
        saveDasChargeFramesP2(dasChargeFrames);
        iterateOverReceivers(SettingKey.DasChargeFramesP2, getDasChargeFramesP2());
    }

    public void setAutoRepeatFramesP2(int autoRepeatFrames) {
        saveAutoRepeatFramesP2(autoRepeatFrames);
        iterateOverReceivers(SettingKey.AutoRepeatFramesP2, getAutoRepeatFramesP2());
    }

    public void setSonicDropP2(boolean sonicDrop) {
        saveSonicDropP2(sonicDrop);
        iterateOverReceivers(SettingKey.SonicDropP2, getSonicDropP2());
    }

    public void setControlSchemeP2(ControlScheme controlScheme) {
        saveControlSchemeP2(controlScheme);
        iterateOverReceivers(SettingKey.ControlSchemeP2, getControlSchemeP2());
    }

    public void setGameplayMode(GameplayMode gameplayMode) {
        saveGameplayMode(gameplayMode);
        iterateOverReceivers(SettingKey.GameplayMode, getGameplayMode());
    }

    public void setBlockSkin(String blockSkin) {
        saveBlockSkin(blockSkin);
        iterateOverReceivers(SettingKey.BlockSkin, getBlockSkin());
    }

    public void setBlockConnectionMode(BlockConnectionMode blockConnectionMode) {
        saveBlockConnectionMode(blockConnectionMode);
        iterateOverReceivers(SettingKey.BlockConnectionMode, getBlockConnectionMode());
    }
}
