package com.fayber.simplecoords;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class CustomConfigScreen extends Screen {
    private final Screen parent;

    public CustomConfigScreen(Screen parent) {
        super(Text.literal("SimpleCoords Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Button to open the drag-and-drop editor
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Edit HUD Position (Drag & Drop)"), button -> {
            if (this.client != null) {
                this.client.setScreen(new HUDEditorScreen(this));
            }
        })
        .dimensions(this.width / 2 - 100, this.height / 2 - 35, 200, 20)
        .build());

        // Simple toggle for Enabled/Disabled
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Status: " + (Config.data.enabled ? "Enabled" : "Disabled")), button -> {
            Config.data.enabled = !Config.data.enabled;
            Config.save();
            button.setMessage(Text.literal("Status: " + (Config.data.enabled ? "Enabled" : "Disabled")));
        })
        .dimensions(this.width / 2 - 100, this.height / 2 - 10, 200, 20)
        .build());

        // Back button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        })
        .dimensions(this.width / 2 - 100, this.height / 2 + 25, 200, 20)
        .build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTick) {
        super.render(drawContext, mouseX, mouseY, partialTick);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
