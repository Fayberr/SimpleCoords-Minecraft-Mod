package com.fayber.simplecoords;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        CoordsHUDOverlay.renderHUD(graphics, 123.456, 64.0, 789.012, "NORTH", Config.data.hud_x, Config.data.hud_y);

        graphics.centeredText(this.font, "Drag the HUD to reposition it", this.width / 2, 10, 0xFFFFFF);
        graphics.centeredText(this.font, "Press ESC to Save & Close", this.width / 2, 20, 0xAAAAAA);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();

        int x = Config.data.hud_x;
        int y = Config.data.hud_y;

        int width = 100;
        width = Math.max(width, this.minecraft.font.width("XYZ: 123.456 / 64.000 / 789.012") + 4);

        if (mouseX >= x - 2 && mouseX <= x + width && mouseY >= y - 2 && mouseY <= y + 32) {
            this.dragging = true;
            this.dragOffsetX = mouseX - x;
            this.dragOffsetY = mouseY - y;
            return true;
        }
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double deltaX, double deltaY) {
        if (this.dragging) {
            int newX = (int) (click.x() - this.dragOffsetX);
            int newY = (int) (click.y() - this.dragOffsetY);

            newX = Math.max(0, Math.min(newX, this.width - 10));
            newY = Math.max(0, Math.min(newY, this.height - 10));

            Config.data.hud_x = newX;
            Config.data.hud_y = newY;
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        this.dragging = false;
        return super.mouseReleased(click);
    }

    @Override
    public void onClose() {
        Config.save();
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
