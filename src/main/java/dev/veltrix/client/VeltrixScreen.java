package dev.veltrix.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public final class VeltrixScreen extends Screen {
    private static final int PANEL = 0xF0141720;
    private static final int PANEL_ALT = 0xFF1B202B;
    private static final int BORDER = 0xFF303847;
    private static final int TEXT = 0xFFF2F5FA;
    private static final int MUTED = 0xFF9BA7B8;
    private static final int ACCENT = 0xFF8AAEFF;
    private static final String[] CATEGORIES = {"All", "PvP", "Exploration", "HUD", "Visuals", "Performance", "Utility"};
    private static final Set<String> ENABLED = new HashSet<>();
    private String selectedCategory = "All";
    private int firstVisible = 0;

    public VeltrixScreen() { super(Component.literal("Veltrix")); }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        int pw = Math.min(960, this.width - 32);
        int ph = Math.min(620, this.height - 28);
        int left = (this.width - pw) / 2;
        int top = (this.height - ph) / 2;

        g.fill(left, top, left + pw, top + ph, PANEL);
        g.fill(left, top, left + 5, top + ph, ACCENT);
        g.fill(left + 1, top + 1, left + pw - 1, top + 68, PANEL_ALT);
        g.fill(left + 1, top + 67, left + pw - 1, top + 68, BORDER);
        g.drawString(font, "VELTRIX", left + 25, top + 18, TEXT, false);
        g.drawString(font, "CLIENT  /  MOD MENU", left + 26, top + 36, MUTED, false);
        g.drawString(font, "RIGHT SHIFT  ·  CLOSE", left + pw - 164, top + 28, MUTED, false);

        int sx = left + 20, sy = top + 91;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int y = sy + i * 37;
            boolean active = CATEGORIES[i].equals(selectedCategory);
            if (active) {
                g.fill(sx, y - 7, sx + 145, y + 21, 0xFF29364B);
                g.fill(sx, y - 7, sx + 3, y + 21, ACCENT);
            }
            g.drawString(font, CATEGORIES[i], sx + 13, y, active ? TEXT : MUTED, false);
        }

        int cx = left + 190;
        g.drawString(font, selectedCategory.equals("All") ? "Explore modules" : selectedCategory, cx, top + 88, TEXT, false);
        g.drawString(font, VeltrixModules.ALL.size() + " modules in the catalogue", cx, top + 105, MUTED, false);

        int cardY = top + 139, gap = 10;
        int cw = (pw - 190 - 40 - gap * 2) / 3;
        int visible = 0;
        int index = 0;
        for (VeltrixModules.Module module : VeltrixModules.ALL) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(module.category())) continue;
            if (index++ < firstVisible) continue;
            if (visible >= 9) break;
            int x = cx + (visible % 3) * (cw + gap);
            int y = cardY + (visible / 3) * 112;
            drawCard(g, x, y, cw, module, mouseX, mouseY);
            visible++;
        }

        g.drawString(font, "CLICK A CARD TO TOGGLE ITS UI STATE", cx, top + ph - 42, MUTED, false);
        g.drawString(font, "Module behavior is being implemented in stages.", cx, top + ph - 27, MUTED, false);
        g.drawString(font, "VELTRIX  •  0.1.1", left + 24, top + ph - 27, MUTED, false);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void drawCard(GuiGraphics g, int x, int y, int w, VeltrixModules.Module module, int mx, int my) {
        boolean enabled = ENABLED.contains(module.name());
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + 94;
        g.fill(x, y, x + w, y + 94, hover ? 0xFF252D3B : PANEL_ALT);
        g.fill(x, y, x + w, y + 1, enabled ? ACCENT : BORDER);
        g.fill(x + 12, y + 12, x + 36, y + 36, enabled ? 0xFF314766 : 0xFF303C50);
        g.drawString(font, "✦", x + 20, y + 20, enabled ? ACCENT : MUTED, false);
        g.drawString(font, module.name(), x + 12, y + 46, TEXT, false);
        g.drawString(font, module.description(), x + 12, y + 62, MUTED, false);
        int sw = 32, sh = 13, tx = x + w - sw - 12, ty = y + 13;
        g.fill(tx, ty, tx + sw, ty + sh, enabled ? 0xFF496D9E : BORDER);
        g.fill(enabled ? tx + 19 : tx + 3, ty + 2, enabled ? tx + 29 : tx + 13, ty + 11, TEXT);
        g.drawString(font, enabled ? "ON" : "OFF", tx - 25, ty + 3, enabled ? ACCENT : MUTED, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int pw = Math.min(960, this.width - 32);
        int ph = Math.min(620, this.height - 28);
        int left = (this.width - pw) / 2, top = (this.height - ph) / 2;
        int sx = left + 20, sy = top + 91;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int y = sy + i * 37;
            if (mouseX >= sx && mouseX <= sx + 145 && mouseY >= y - 7 && mouseY <= y + 21) {
                selectedCategory = CATEGORIES[i];
                firstVisible = 0;
                return true;
            }
        }

        int cx = left + 190, cardY = top + 139, gap = 10;
        int cw = (pw - 190 - 40 - gap * 2) / 3;
        int visible = 0, index = 0;
        for (VeltrixModules.Module module : VeltrixModules.ALL) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(module.category())) continue;
            if (index++ < firstVisible) continue;
            if (visible >= 9) break;
            int x = cx + (visible % 3) * (cw + gap);
            int y = cardY + (visible / 3) * 112;
            if (mouseX >= x && mouseX <= x + cw && mouseY >= y && mouseY <= y + 94) {
                if (!ENABLED.add(module.name())) ENABLED.remove(module.name());
                return true;
            }
            visible++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            onClose();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) { firstVisible += 9; return true; }
        if (keyCode == GLFW.GLFW_KEY_PAGE_UP) { firstVisible = Math.max(0, firstVisible - 9); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean isPauseScreen() { return false; }
}
