package com.fayber.simplecoords;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.fml.ModContainer;
import net.minecraft.util.Mth;

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
        int centerX = this.width / 2;
        int startY = 25;
        int spacing = 22;

        // 1. Enabled Toggle
        this.addRenderableWidget(Button.builder(getToggleText("HUD Enabled", Config.ENABLED.get()), button -> {
            Config.ENABLED.set(!Config.ENABLED.get());
            Config.ENABLED.save();
            button.setMessage(getToggleText("HUD Enabled", Config.ENABLED.get()));
        })
        .bounds(centerX - 100, startY, 200, 20)
        .build());

        // 2. Use Camera Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Use Camera Pos", Config.USE_CAMERA.get()), button -> {
            Config.USE_CAMERA.set(!Config.USE_CAMERA.get());
            Config.USE_CAMERA.save();
            button.setMessage(getToggleText("Use Camera Pos", Config.USE_CAMERA.get()));
        })
        .bounds(centerX - 100, startY + spacing, 200, 20)
        .build());

        // 3. Show XYZ Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Show XYZ", Config.SHOW_XYZ.get()), button -> {
            Config.SHOW_XYZ.set(!Config.SHOW_XYZ.get());
            Config.SHOW_XYZ.save();
            button.setMessage(getToggleText("Show XYZ", Config.SHOW_XYZ.get()));
        })
        .bounds(centerX - 100, startY + spacing * 2, 200, 20)
        .build());

        // 4. Show Subchunk Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Show Subchunk", Config.SHOW_SUBCHUNK.get()), button -> {
            Config.SHOW_SUBCHUNK.set(!Config.SHOW_SUBCHUNK.get());
            Config.SHOW_SUBCHUNK.save();
            button.setMessage(getToggleText("Show Subchunk", Config.SHOW_SUBCHUNK.get()));
        })
        .bounds(centerX - 100, startY + spacing * 3, 200, 20)
        .build());

        // 5. Show Facing Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Show Facing", Config.SHOW_FACING.get()), button -> {
            Config.SHOW_FACING.set(!Config.SHOW_FACING.get());
            Config.SHOW_FACING.save();
            button.setMessage(getToggleText("Show Facing", Config.SHOW_FACING.get()));
        })
        .bounds(centerX - 100, startY + spacing * 4, 200, 20)
        .build());

        // 6. Show Intercardinal Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Intercardinal Directions", Config.SHOW_INTERCARDINAL.get()), button -> {
            Config.SHOW_INTERCARDINAL.set(!Config.SHOW_INTERCARDINAL.get());
            Config.SHOW_INTERCARDINAL.save();
            button.setMessage(getToggleText("Intercardinal Directions", Config.SHOW_INTERCARDINAL.get()));
        })
        .bounds(centerX - 100, startY + spacing * 5, 200, 20)
        .build());

        // 7. Show Background Toggle
        this.addRenderableWidget(Button.builder(getToggleText("Show Background", Config.SHOW_BACKGROUND.get()), button -> {
            Config.SHOW_BACKGROUND.set(!Config.SHOW_BACKGROUND.get());
            Config.SHOW_BACKGROUND.save();
            button.setMessage(getToggleText("Show Background", Config.SHOW_BACKGROUND.get()));
        })
        .bounds(centerX - 100, startY + spacing * 6, 200, 20)
        .build());

        // 8. Precision Slider
        this.addRenderableWidget(new AbstractSliderButton(centerX - 100, startY + spacing * 7, 200, 20, Component.literal("Decimal Precision: " + Config.COORD_PRECISION.get()), (double) Config.COORD_PRECISION.get() / 5.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Decimal Precision: " + Config.COORD_PRECISION.get()));
            }

            @Override
            protected void applyValue() {
                Config.COORD_PRECISION.set((int) Math.round(this.value * 5.0));
                Config.COORD_PRECISION.save();
            }
        });

        // 9. HUD Editor Button
        this.addRenderableWidget(Button.builder(Component.literal("Edit HUD Position (Drag & Drop)"), button -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(new HUDEditorScreen(this));
            }
        })
        .bounds(centerX - 100, startY + spacing * 8, 200, 20)
        .build());

        // Back button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.parent);
            }
        })
        .bounds(centerX - 100, this.height - 30, 200, 20)
        .build());
    }

    private Component getToggleText(String prefix, boolean value) {
        return Component.literal(prefix + ": " + (value ? "ON" : "OFF"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
