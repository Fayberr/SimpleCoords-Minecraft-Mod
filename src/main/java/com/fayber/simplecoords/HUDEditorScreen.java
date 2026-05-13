package com.fayber.simplecoords;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HUDEditorScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public HUDEditorScreen(Screen parent) {
        super(Text.literal("HUD Editor"));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTick) {
        super.render(drawContext, mouseX, mouseY, partialTick);

        CoordsHUDOverlay.renderHUD(drawContext, 123.456, 64.0, 789.012, "NORTH", Config.data.hud_x, Config.data.hud_y);
        
        drawContext.drawCenteredTextWithShadow(this.textRenderer, "Drag the HUD to reposition it", this.width / 2, 10, 0xFFFFFF);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, "Press ESC to Save & Close", this.width / 2, 20, 0xAAAAAA);
    }

    @Override
    public boolean mouseClicked(Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        
        int x = Config.data.hud_x;
        int y = Config.data.hud_y;
        
        if (mouseX >= x && mouseX <= x + 150 && mouseY >= y && mouseY <= y + 30) {
            this.dragging = true;
            this.dragOffsetX = mouseX - x;
            this.dragOffsetY = mouseY - y;
            return true;
        }
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
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
    public boolean mouseReleased(Click click) {
        this.dragging = false;
        return super.mouseReleased(click);
    }

    @Override
    public void close() {
        Config.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return true;
    }
}
