package dev.veltrix.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class VeltrixScreen extends Screen {
    private static final int PANEL = 0xF0141720;
    private static final int PANEL_ALT = 0xFF1B202B;
    private static final int BORDER = 0xFF303847;
    private static final int TEXT = 0xFFF2F5FA;
    private static final int MUTED = 0xFF9BA7B8;
    private static final int ACCENT = 0xFF8AAEFF;
    private String selectedCategory = "All";

    public VeltrixScreen() { super(Component.literal("Veltrix")); }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        int pw = Math.min(920, this.width - 48);
        int ph = Math.min(590, this.height - 40);
        int left = (this.width - pw) / 2;
        int top = (this.height - ph) / 2;

        g.fill(left, top, left + pw, top + ph, PANEL);
        g.fill(left, top, left + 5, top + ph, ACCENT);
        g.fill(left + 1, top + 1, left + pw - 1, top + 66, PANEL_ALT);
        g.fill(left + 1, top + 65, left + pw - 1, top + 66, BORDER);
        g.drawString(font, "VELTRIX", left + 26, top + 19, TEXT, false);
        g.drawString(font, "CLIENT  /  MOD MENU", left + 27, top + 37, MUTED, false);
        g.drawString(font, "RIGHT SHIFT  ·  CLOSE", left + pw - 164, top + 27, MUTED, false);

        int sx = left + 20, sy = top + 88;
        String[] cats = {"All", "Visuals", "HUD", "Performance", "Utility"};
        for (int i = 0; i < cats.length; i++) {
            int y = sy + i * 38;
            boolean active = cats[i].equals(selectedCategory);
            if (active) {
                g.fill(sx, y - 7, sx + 142, y + 21, 0xFF29364B);
                g.fill(sx, y - 7, sx + 3, y + 21, ACCENT);
            }
            g.drawString(font, cats[i], sx + 13, y, active ? TEXT : MUTED, false);
        }

        int cx = left + 190;
        g.drawString(font, selectedCategory.equals("All") ? "Your modules" : selectedCategory, cx, top + 88, TEXT, false);
        g.drawString(font, "A clean workspace for the tools you actually use", cx, top + 105, MUTED, false);

        int cardY = top + 139, gap = 12;
        int cw = (pw - 190 - 42 - gap * 2) / 3;
        String[][] modules = {
            {"Armor Status", "HUD", "Armor and durability display"},
            {"Keystrokes", "HUD", "Movement and action keys"},
            {"FPS Counter", "Performance", "Current frame rate"},
            {"Coordinates", "HUD", "Current world position"},
            {"Time Display", "Visuals", "Current world time"},
            {"Fullbright", "Visuals", "Improved dark-area visibility"}
        };
        int visible = 0;
        for (String[] m : modules) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(m[1])) continue;
            int x = cx + (visible % 3) * (cw + gap);
            int y = cardY + (visible / 3) * 112;
            drawCard(g, x, y, cw, m[0], m[2], mouseX, mouseY);
            visible++;
        }
        g.drawString(font, "VELTRIX  •  BUILD 0.1.0", left + 24, top + ph - 24, MUTED, false);
        g.drawString(font, "Only implemented modules will be marked as working.", cx, top + ph - 24, MUTED, false);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void drawCard(GuiGraphics g, int x, int y, int w, String name, String desc, int mx, int my) {
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + 94;
        g.fill(x, y, x + w, y + 94, hover ? 0xFF252D3B : PANEL_ALT);
        g.fill(x, y, x + w, y + 1, hover ? ACCENT : BORDER);
        g.fill(x + 13, y + 13, x + 39, y + 39, 0xFF303C50);
        g.drawString(font, "✦", x + 21, y + 21, ACCENT, false);
        g.drawString(font, name, x + 13, y + 49, TEXT, false);
        g.drawString(font, desc, x + 13, y + 66, MUTED, false);
        g.fill(x + w - 29, y + 13, x + w - 13, y + 29, BORDER);
        g.drawString(font, "⚙", x + w - 25, y + 17, MUTED, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int pw = Math.min(920, this.width - 48);
        int ph = Math.min(590, this.height - 40);
        int left = (this.width - pw) / 2, top = (this.height - ph) / 2;
        int sx = left + 20, sy = top + 88;
        String[] cats = {"All", "Visuals", "HUD", "Performance", "Utility"};
        for (int i = 0; i < cats.length; i++) {
            int y = sy + i * 38;
            if (mouseX >= sx && mouseX <= sx + 142 && mouseY >= y - 7 && mouseY <= y + 21) {
                selectedCategory = cats[i];
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean isPauseScreen() { return false; }
}
