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
import org.lwjgl.glfw.GLFW;

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
        if (client.player == null || false) return;

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
            long ticks = client.level.getLevelData().getDayTime() % 24000L;
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

        if (VeltrixModuleState.isEnabled("Keystrokes")) {
            long window = client.getWindow().handle();
            String[] keys = {"W", "A", "S", "D"};
            int[] codes = {GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D};
            int startX = 8;
            int keyY = y + 2;
            for (int i = 0; i < keys.length; i++) {
                boolean down = GLFW.glfwGetKey(window, codes[i]) == GLFW.GLFW_PRESS;
                int bx = i == 0 ? startX + 23 : startX + ((i - 1) % 3) * 23;
                int by = keyY + (i == 0 ? 0 : 22);
                graphics.fill(bx, by, bx + 20, by + 19, down ? 0xDDA8C7EE : GLASS);
                graphics.text(client.font, keys[i], bx + 7, by + 5, INK, false);
            }
            boolean jump = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
            graphics.fill(startX, keyY + 44, startX + 66, keyY + 62, jump ? 0xDDA8C7EE : GLASS);
            graphics.text(client.font, "SPACE", startX + 20, keyY + 49, INK, false);
        }
    }
}
