package dev.veltrix.client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class VeltrixModuleState {
    private static final Set<String> ENABLED = ConcurrentHashMap.newKeySet();
    private static final Set<String> IMPLEMENTED = Set.of(
            "FPS Counter", "Coordinates", "Direction HUD", "Clock", "Keystrokes",
            "Custom Block Highlight"
    );

    private VeltrixModuleState() {}

    public static boolean isImplemented(String name) { return IMPLEMENTED.contains(name); }
    public static boolean isEnabled(String name) { return ENABLED.contains(name); }
    public static void toggle(String name) {
        if (!isImplemented(name) || name.equals("Custom Block Highlight")) return;
        if (!ENABLED.add(name)) ENABLED.remove(name);
    }
}