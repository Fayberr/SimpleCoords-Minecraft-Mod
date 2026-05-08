package com.fayber.simplecoords;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HUDEditorScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public HUDEditorScreen(Screen parent) {
        super(Component.literal("HUD Editor"));
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Render the background (includes the vanilla blur and darkening)
        // super.render will call renderBackground/renderTransparentBackground
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // 2. Render our UI elements AFTER super.render() so they are drawn on top of the blur
        
        // Render the HUD at its current configured position
        CoordsHUDOverlay.renderHUD(guiGraphics, 123.456, 64.0, 789.012, "NORTH", Config.HUD_X.get(), Config.HUD_Y.get());
        
        // Draw instructions
        guiGraphics.drawCenteredString(this.font, "Drag the HUD to reposition it", this.width / 2, 10, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "Press ESC to Save & Close", this.width / 2, 20, 0xAAAAAA);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = Config.HUD_X.get();
        int y = Config.HUD_Y.get();
        
        // Rough hit-box for the HUD (assume 150x30 for now)
        if (mouseX >= x && mouseX <= x + 150 && mouseY >= y && mouseY <= y + 30) {
            this.dragging = true;
            this.dragOffsetX = mouseX - x;
            this.dragOffsetY = mouseY - y;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.dragging) {
            int newX = (int) (mouseX - this.dragOffsetX);
            int newY = (int) (mouseY - this.dragOffsetY);
            
            // Constrain to screen
            newX = Math.max(0, Math.min(newX, this.width - 10));
            newY = Math.max(0, Math.min(newY, this.height - 10));
            
            Config.HUD_X.set(newX);
            Config.HUD_Y.set(newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        Config.HUD_X.save();
        Config.HUD_Y.save();
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
