package dev.veltrix.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class VeltrixScreen extends Screen {
    private static final int PANEL = 0xDDF8FAFD;
    private static final int PANEL_ALT = 0xBFFFFFFF;
    private static final int CARD = 0xAFFFFFFF;
    private static final int CARD_HOVER = 0xE8FFFFFF;
    private static final int BORDER = 0x66A9B7C9;
    private static final int TEXT = 0xFF263447;
    private static final int MUTED = 0xFF718096;
    private static final int ACCENT = 0xFF7299C8;
    private static final String[] CATEGORIES = {"All", "PvP", "Exploration", "HUD", "Visuals", "Performance", "Utility"};
    private String selectedCategory = "All";
    private int firstVisible = 0;

    public VeltrixScreen() { super(Component.literal("Veltrix")); }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(g, mouseX, mouseY, partialTick);
        int pw = Math.min(780, this.width - 24);
        int ph = Math.min(500, this.height - 24);
        int left = (this.width - pw) / 2;
        int top = (this.height - ph) / 2;

        g.fill(0, 0, this.width, this.height, 0x300B1422);
        g.fill(left + 4, top + 5, left + pw + 4, top + ph + 5, 0x26000000);
        g.fill(left, top, left + pw, top + ph, PANEL);
        g.outline(left, top, pw, ph, 0xA6FFFFFF);
        g.fill(left + 1, top + 1, left + pw - 1, top + 57, PANEL_ALT);
        g.fill(left + 1, top + 56, left + pw - 1, top + 57, BORDER);

        g.text(font, "VELTRIX", left + 20, top + 13, TEXT, false);
        g.text(font, "A CLEANER WAY TO PLAY", left + 21, top + 31, MUTED, false);
        g.text(font, "RIGHT SHIFT  ·  CLOSE", left + pw - 150, top + 23, MUTED, false);

        int sx = left + 14, sy = top + 77;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int y = sy + i * 30;
            boolean active = CATEGORIES[i].equals(selectedCategory);
            if (active) {
                g.fill(sx, y - 6, sx + 120, y + 17, 0xA9DCE8F8);
                g.fill(sx, y - 6, sx + 3, y + 17, ACCENT);
            }
            g.text(font, CATEGORIES[i], sx + 11, y, active ? TEXT : MUTED, false);
        }

        int cx = left + 150;
        g.text(font, selectedCategory.equals("All") ? "Modules" : selectedCategory, cx, top + 73, TEXT, false);
        g.text(font, "Working modules are enabled here; others are marked coming soon.", cx, top + 88, MUTED, false);

        int cardY = top + 111, gap = 10;
        int cw = (pw - 150 - 30 - gap) / 2;
        int visible = 0, index = 0;
        for (VeltrixModules.Module module : VeltrixModules.ALL) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(module.category())) continue;
            if (index++ < firstVisible) continue;
            if (visible >= 8) break;
            int x = cx + (visible % 2) * (cw + gap);
            int y = cardY + (visible / 2) * 78;
            drawCard(g, x, y, cw, module, mouseX, mouseY);
            visible++;
        }

        g.text(font, "PAGE UP / PAGE DOWN  ·  BROWSE MODULES", left + 18, top + ph - 20, MUTED, false);
        g.text(font, "VELTRIX  •  0.1.1", left + pw - 112, top + ph - 20, MUTED, false);
    }

    private void drawCard(GuiGraphicsExtractor g, int x, int y, int w, VeltrixModules.Module module, int mx, int my) {
        boolean implemented = VeltrixModuleState.isImplemented(module.name());
        boolean enabled = module.name().equals("Custom Block Highlight")
                ? BlockHighlightSettings.enabled : VeltrixModuleState.isEnabled(module.name());
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + 68;
        g.fill(x, y, x + w, y + 68, hover ? CARD_HOVER : CARD);
        g.outline(x, y, w, 68, BORDER);
        g.fill(x + 10, y + 11, x + 31, y + 32, enabled ? 0xFFDCEBFA : 0xFFE9EEF4);
        g.text(font, "✦", x + 16, y + 17, enabled ? ACCENT : MUTED, false);
        g.text(font, module.name(), x + 40, y + 10, TEXT, false);
        g.text(font, module.description(), x + 40, y + 27, MUTED, false);

        if (!implemented) {
            g.text(font, "COMING SOON", x + 40, y + 46, MUTED, false);
            return;
        }
        if (module.name().equals("Custom Block Highlight")) {
            g.text(font, (BlockHighlightSettings.rainbow ? "RAINBOW" : "CUSTOM") + "  " + String.format("%.1f", BlockHighlightSettings.outlineWidth) + "px", x + 40, y + 46, ACCENT, false);
            g.fill(x + w - 57, y + 43, x + w - 39, y + 59, 0xFFDCE8F5);
            g.text(font, "F", x + w - 51, y + 47, BlockHighlightSettings.fillEnabled ? ACCENT : MUTED, false);
        }
        int tx = x + w - 35, ty = y + 11;
        g.fill(tx, ty, tx + 25, ty + 12, enabled ? 0xFF91B4DE : 0xFFD9E0E9);
        g.fill(enabled ? tx + 14 : tx + 2, ty + 2, enabled ? tx + 23 : tx + 11, ty + 10, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x(), mouseY = event.y();
        int button = event.button();
        int pw = Math.min(780, this.width - 24);
        int ph = Math.min(500, this.height - 24);
        int left = (this.width - pw) / 2, top = (this.height - ph) / 2;
        int sx = left + 14, sy = top + 77;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int y = sy + i * 30;
            if (mouseX >= sx && mouseX <= sx + 120 && mouseY >= y - 6 && mouseY <= y + 17) {
                selectedCategory = CATEGORIES[i];
                firstVisible = 0;
                return true;
            }
        }

        int cx = left + 150, cardY = top + 111, gap = 10;
        int cw = (pw - 150 - 30 - gap) / 2;
        int visible = 0, index = 0;
        for (VeltrixModules.Module module : VeltrixModules.ALL) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(module.category())) continue;
            if (index++ < firstVisible) continue;
            if (visible >= 8) break;
            int x = cx + (visible % 2) * (cw + gap);
            int y = cardY + (visible / 2) * 78;
            if (mouseX >= x && mouseX <= x + cw && mouseY >= y && mouseY <= y + 68) {
                if (!VeltrixModuleState.isImplemented(module.name())) return true;
                if (module.name().equals("Custom Block Highlight")) {
                    if (mouseX >= x + cw - 61 && mouseY >= y + 40) BlockHighlightSettings.toggleFill();
                    else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) BlockHighlightSettings.toggleRainbow();
                    else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) BlockHighlightSettings.cycleColor();
                    else BlockHighlightSettings.toggleEnabled();
                } else {
                    VeltrixModuleState.toggle(module.name());
                }
                return true;
            }
            visible++;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int pw = Math.min(780, this.width - 24);
        int ph = Math.min(500, this.height - 24);
        int left = (this.width - pw) / 2, top = (this.height - ph) / 2;
        int cx = left + 150, cardY = top + 111, gap = 10;
        int cw = (pw - 150 - 30 - gap) / 2;
        int visible = 0, index = 0;
        for (VeltrixModules.Module module : VeltrixModules.ALL) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(module.category())) continue;
            if (index++ < firstVisible) continue;
            if (visible >= 8) break;
            int x = cx + (visible % 2) * (cw + gap);
            int y = cardY + (visible / 2) * 78;
            if (module.name().equals("Custom Block Highlight")
                    && mouseX >= x && mouseX <= x + cw && mouseY >= y && mouseY <= y + 68) {
                if (mouseX >= x + cw - 61 && mouseY >= y + 40)
                    BlockHighlightSettings.adjustFillOpacity((float) (verticalAmount * 0.04));
                else BlockHighlightSettings.adjustWidth((float) (verticalAmount * 0.25));
                return true;
            }
            visible++;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape() || event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            onClose();
            return true;
        }
        if (event.key() == GLFW.GLFW_KEY_PAGE_DOWN) { firstVisible += 8; return true; }
        if (event.key() == GLFW.GLFW_KEY_PAGE_UP) { firstVisible = Math.max(0, firstVisible - 8); return true; }
        return super.keyPressed(event);
    }

    @Override public boolean isPauseScreen() { return false; }
}
