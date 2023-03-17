package Tetris;

import java.util.prefs.Preferences;

public class Settings {
    private static final String KEY_DAS_CHARGE_FRAMES = "dasChargeFrames";
    private static final String KEY_AUTO_REPEAT_FRAMES = "autoRepeatFrames";
    private static final String KEY_SONIC_DROP = "sonicDrop";
    private static final String KEY_CONTROL_SCHEME = "controlScheme";
    private Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    private int dasChargeFrames = prefs.getInt(KEY_DAS_CHARGE_FRAMES, 9);
    private int autoRepeatFrames = prefs.getInt(KEY_AUTO_REPEAT_FRAMES, 3);
    private boolean sonicDrop = prefs.getBoolean(KEY_SONIC_DROP, false);
    private String controlScheme = prefs.get(KEY_CONTROL_SCHEME, ControlScheme.WASD.name());

}
