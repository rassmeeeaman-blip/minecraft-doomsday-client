package com.doomsday.ui.screen;

import com.doomsday.DoomsdayClientEvents;
import com.doomsday.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class DoomsdayMainScreen extends Screen {
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 450;
    private int panelX;
    private int panelY;
    private List<Module> modules;
    private int scrollOffset = 0;

    public DoomsdayMainScreen(Screen parent) {
        super(Text.literal("Doomsday Client"));
        this.modules = DoomsdayClientEvents.moduleManager.getModules();
    }

    @Override
    protected void init() {
        super.init();
        
        // Clear previous children
        this.clearChildren();
        
        // Center the panel
        panelX = (this.width - PANEL_WIDTH) / 2;
        panelY = (this.height - PANEL_HEIGHT) / 2;

        // Add module toggle buttons
        int buttonY = panelY + 35;
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int index = i;
            
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal(getModuleText(module)),
                    btn -> toggleModule(modules.get(index))
            ).dimensions(panelX + 15, buttonY, PANEL_WIDTH - 30, 25).build();
            
            this.addDrawableChild(button);
            buttonY += 30;
        }

        // Close button
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close (Shift+Right)"),
                button -> this.close()
        ).dimensions(panelX + 15, panelY + PANEL_HEIGHT - 35, PANEL_WIDTH - 30, 25).build());
    }

    private String getModuleText(Module module) {
        String status = module.isEnabled() ? "§2ON" : "§cOFF";
        return module.getName() + " " + status;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw semi-transparent background
        context.fill(0, 0, this.width, this.height, 0xA0000000);

        // Draw panel background (dark red/black)
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFF1A1A1A);
        
        // Draw panel border (red glow)
        drawBorder(context, panelX - 1, panelY - 1, PANEL_WIDTH + 2, PANEL_HEIGHT + 2, 0xFFCC0000);
        drawBorder(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 0xFF990000);

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, "Doomsday Client", 
                panelX + PANEL_WIDTH / 2, panelY + 12, 0xFFFF0000);
        
        // Draw version
        context.drawCenteredTextWithShadow(this.textRenderer, "v1.0.0", 
                panelX + PANEL_WIDTH / 2, panelY + 23, 0xFFAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }
    
    private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        // Top
        context.fill(x, y, x + width, y + 1, color);
        // Bottom
        context.fill(x, y + height - 1, x + width, y + height, color);
        // Left
        context.fill(x, y, x + 1, y + height, color);
        // Right
        context.fill(x + width - 1, y, x + width, y + height, color);
    }

    private void toggleModule(Module module) {
        module.toggle();
        this.init(); // Refresh buttons
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
