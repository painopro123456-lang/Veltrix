package dev.veltrix.client;

import java.util.ArrayList;
import java.util.List;

public final class VeltrixModules {
    public record Module(String name, String category, String description) {}
    public static final List<Module> ALL = new ArrayList<>();
    private VeltrixModules() {}

    static {
        add("Armor HUD", "HUD", "Armor pieces and durability");
        add("Coordinates", "HUD", "Current XYZ position");
        add("FPS Counter", "HUD", "Frames per second");
        add("Keystrokes", "HUD", "Movement and action keys");
        add("CPS Counter", "HUD", "Left and right clicks per second");
        add("Ping Display", "HUD", "Network latency");
        add("Direction HUD", "HUD", "Compass direction");
        add("Clock", "HUD", "Local clock display");
        add("Potion HUD", "HUD", "Active effects and timers");
        add("Durability HUD", "HUD", "Held item durability");

        add("Combo Counter", "PvP", "Track consecutive hits");
        add("Hit Color", "PvP", "Highlight damage feedback");
        add("Crosshair", "PvP", "Customizable crosshair");
        add("Reach Display", "PvP", "Practice-oriented distance readout");
        add("Attack Indicator", "PvP", "Attack cooldown display");
        add("Totem Counter", "PvP", "Remaining totems in inventory");
        add("Pearl Timer", "PvP", "Ender pearl cooldown tracker");
        add("Armor Durability", "PvP", "Opponent armor information when available");
        add("Potion Timers", "PvP", "Track useful effect durations");
        add("Target Info", "PvP", "Focused entity information");

        add("Biome Display", "Exploration", "Current biome name");
        add("Waypoint Manager", "Exploration", "Save and navigate to locations");
        add("Cave Finder", "Exploration", "Exploration-oriented cave mapping");
        add("Light Level", "Exploration", "Block light at your position");
        add("Entity Radar", "Exploration", "Nearby entity awareness");
        add("World Time", "Exploration", "Current world time");
        add("Village Info", "Exploration", "Nearby village points of interest");
        add("Structure Notes", "Exploration", "Track discovered structures");
        add("Death Coordinates", "Exploration", "Remember your last death location");
        add("Chunk Borders", "Exploration", "Visualize chunk boundaries");

        add("Fullbright", "Visuals", "Improve visibility in dark areas");
        add("Time Changer", "Visuals", "Client-side time presentation");
        add("Weather Visuals", "Visuals", "Client-side weather presentation");
        add("Zoom", "Visuals", "Configurable camera zoom");
        add("Damage Tint", "Visuals", "Customize damage flash");
        add("Custom Block Highlight", "Visuals", "Custom outline, face fill and rainbow colors");

        add("Chunk Updates", "Performance", "Client rendering performance controls");
        add("Entity Culling", "Performance", "Avoid rendering hidden entities");
        add("Particle Limiter", "Performance", "Reduce particle load");
        add("FPS Limiter", "Performance", "Configure frame-rate cap");
        add("Animation Options", "Performance", "Reduce selected visual animations");

        add("Auto GG", "Utility", "Send a configured post-game message");
        add("Chat Timestamps", "Utility", "Show message timestamps");
        add("Quick Messages", "Utility", "Configurable chat shortcuts");
        add("Screenshot Manager", "Utility", "Organize screenshot shortcuts");
        add("Inventory Profiles", "Utility", "Convenient inventory layout tools");
    }

    private static void add(String name, String category, String description) {
        ALL.add(new Module(name, category, description));
    }
}
