package dev.veltrix.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class BlockHighlightSettings {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("veltrix-block-highlight.properties");

    public static boolean enabled = true;
    public static boolean fillEnabled = false;
    public static boolean rainbow = false;
    public static boolean smooth = true;
    public static float outlineWidth = 2.0f;
    public static float fillOpacity = 0.22f;
    public static int outlineColor = 0xFF82B5FF;
    public static int fillColor = 0x4082B5FF;

    private BlockHighlightSettings() {}

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(FILE)) {
            p.load(in);
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
            fillEnabled = Boolean.parseBoolean(p.getProperty("fillEnabled", "false"));
            rainbow = Boolean.parseBoolean(p.getProperty("rainbow", "false"));
            smooth = Boolean.parseBoolean(p.getProperty("smooth", "true"));
            outlineWidth = clamp(Float.parseFloat(p.getProperty("outlineWidth", "2.0")), 0.5f, 8.0f);
            fillOpacity = clamp(Float.parseFloat(p.getProperty("fillOpacity", "0.22")), 0.02f, 0.8f);
            outlineColor = (int) Long.parseLong(p.getProperty("outlineColor", "FF82B5FF"), 16);
            fillColor = (int) Long.parseLong(p.getProperty("fillColor", "4082B5FF"), 16);
        } catch (IOException | NumberFormatException ignored) {
            // Keep safe defaults if the user's config is damaged.
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("enabled", Boolean.toString(enabled));
        p.setProperty("fillEnabled", Boolean.toString(fillEnabled));
        p.setProperty("rainbow", Boolean.toString(rainbow));
        p.setProperty("smooth", Boolean.toString(smooth));
        p.setProperty("outlineWidth", Float.toString(outlineWidth));
        p.setProperty("fillOpacity", Float.toString(fillOpacity));
        p.setProperty("outlineColor", String.format("%08X", outlineColor));
        p.setProperty("fillColor", String.format("%08X", fillColor));
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) {
                p.store(out, "Veltrix custom block highlight");
            }
        } catch (IOException ignored) {
            // Config saving must never interrupt rendering or gameplay.
        }
    }

    public static void toggleEnabled() { enabled = !enabled; save(); }
    public static void toggleFill() { fillEnabled = !fillEnabled; save(); }
    public static void toggleRainbow() { rainbow = !rainbow; save(); }
    public static void toggleSmooth() { smooth = !smooth; save(); }

    public static void cycleColor() {
        int[] colors = {0xFF82B5FF, 0xFFB58CFF, 0xFFFFC46B, 0xFF72E0B1, 0xFFFF8299};
        int next = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == outlineColor) { next = (i + 1) % colors.length; break; }
        }
        outlineColor = colors[next];
        fillColor = (Math.round(fillOpacity * 255.0f) << 24) | (outlineColor & 0x00FFFFFF);
        save();
    }

    public static int currentOutlineColor(long now) {
        if (!rainbow) return outlineColor;
        float hue = (now % 5000L) / 5000.0f;
        return 0xFF000000 | hsv(hue);
    }

    private static int hsv(float h) {
        float scaled = h * 6.0f;
        int sector = (int) scaled;
        float f = scaled - sector;
        float q = 1.0f - f;
        float t = f;
        float r, g, b;
        switch (sector % 6) {
            case 0 -> { r = 1; g = t; b = 0; }
            case 1 -> { r = q; g = 1; b = 0; }
            case 2 -> { r = 0; g = 1; b = t; }
            case 3 -> { r = 0; g = q; b = 1; }
            case 4 -> { r = t; g = 0; b = 1; }
            default -> { r = 1; g = 0; b = q; }
        }
        return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
