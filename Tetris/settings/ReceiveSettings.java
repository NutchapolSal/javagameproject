package Tetris.settings;

import java.util.Map;
import java.util.function.Consumer;

public interface ReceiveSettings {
    /**
     * each entry in the returned map is a {@code Consumer<Object>} that will have a
     * side effect of modifying the implementer's state
     * <p>
     * the class of the {@code Object} sent to the {@code Consumer<Object>} depends
     * on the associated {@code SettingKey}, implementers can safely cast the
     * {@code Object} to the specified class
     * <p>
     * there must be at least one entry in the map
     */
    public Map<SettingKey, Consumer<Object>> getReceivers();
}
