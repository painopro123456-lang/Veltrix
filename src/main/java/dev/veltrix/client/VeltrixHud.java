package dev.veltrix.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class VeltrixHud {
    private static final int INK = 0xFF263447;
    private static final int GLASS = 0xBFFFFFFF;
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    private VeltrixHud() {}

    public static void register() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("veltrix", "module_hud"),
                VeltrixHud::render
        );
    }

    private static void render(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) return;

        List<String> lines = new ArrayList<>();
        if (VeltrixModuleState.isEnabled("FPS Counter")) {
            lines.add("FPS  " + client.getFps());
        }
        if (VeltrixModuleState.isEnabled("Coordinates")) {
            var p = client.player;
            lines.add(String.format("XYZ  %.1f  %.1f  %.1f", p.getX(), p.getY(), p.getZ()));
        }
        if (VeltrixModuleState.isEnabled("Direction HUD")) {
            lines.add("FACING  " + client.player.getDirection().getName().toUpperCase());
        }
        if (VeltrixModuleState.isEnabled("Clock")) {
            lines.add("TIME  " + LocalTime.now().format(CLOCK));
        }
        if (VeltrixModuleState.isEnabled("World Time")) {
            long ticks = client.level.getDayTime() % 24000L;
            long hours = ((ticks / 1000L + 6L) % 24L);
            long minutes = (ticks % 1000L) * 60L / 1000L;
            lines.add(String.format("WORLD  %02d:%02d", hours, minutes));
        }

        int y = 8;
        for (String line : lines) {
            int width = client.font.width(line) + 16;
            graphics.fill(8, y, 8 + width, y + 17, GLASS);
            graphics.fill(8, y, 10, y + 17, 0xFF91B5DF);
            graphics.text(client.font, line, 15, y + 5, INK, false);
            y += 21;
        }
    }
}
