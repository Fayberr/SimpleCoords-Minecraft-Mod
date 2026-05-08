package com.fayber.simplecoords;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class CustomConfigScreen extends Screen {
    private final ModContainer container;
    private final Screen parent;

    public CustomConfigScreen(ModContainer container, Screen parent) {
        super(Component.literal("SimpleCoords Configuration"));
        this.container = container;
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Button to open the drag-and-drop editor
        this.addRenderableWidget(Button.builder(Component.literal("Edit HUD Position (Drag & Drop)"), button -> {
            this.minecraft.setScreen(new HUDEditorScreen(this));
        })
        .bounds(this.width / 2 - 100, this.height / 2 - 35, 200, 20)
        .build());

        // Button to open the standard NeoForge config list
        this.addRenderableWidget(Button.builder(Component.literal("Configure HUD Options"), button -> {
            this.minecraft.setScreen(new ConfigurationScreen(this.container, this));
        })
        .bounds(this.width / 2 - 100, this.height / 2 - 10, 200, 20)
        .build());

        // Back button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> {
            this.minecraft.setScreen(this.parent);
        })
        .bounds(this.width / 2 - 100, this.height / 2 + 25, 200, 20)
        .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}
