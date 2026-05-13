package com.fayber.simplecoords;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
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
        int centerX = this.width / 2;
        int startY = 25;
        int spacing = 22;

        // 1. Enabled Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("HUD Enabled", Config.data.enabled), button -> {
            Config.data.enabled = !Config.data.enabled;
            Config.save();
            button.setMessage(getToggleText("HUD Enabled", Config.data.enabled));
        })
        .dimensions(centerX - 100, startY, 200, 20)
        .build());

        // 2. Use Camera Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Use Camera Pos", Config.data.use_camera), button -> {
            Config.data.use_camera = !Config.data.use_camera;
            Config.save();
            button.setMessage(getToggleText("Use Camera Pos", Config.data.use_camera));
        })
        .dimensions(centerX - 100, startY + spacing, 200, 20)
        .build());

        // 3. Show XYZ Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Show XYZ", Config.data.show_xyz), button -> {
            Config.data.show_xyz = !Config.data.show_xyz;
            Config.save();
            button.setMessage(getToggleText("Show XYZ", Config.data.show_xyz));
        })
        .dimensions(centerX - 100, startY + spacing * 2, 200, 20)
        .build());

        // 4. Show Subchunk Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Show Subchunk", Config.data.show_subchunk), button -> {
            Config.data.show_subchunk = !Config.data.show_subchunk;
            Config.save();
            button.setMessage(getToggleText("Show Subchunk", Config.data.show_subchunk));
        })
        .dimensions(centerX - 100, startY + spacing * 3, 200, 20)
        .build());

        // 5. Show Facing Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Show Facing", Config.data.show_facing), button -> {
            Config.data.show_facing = !Config.data.show_facing;
            Config.save();
            button.setMessage(getToggleText("Show Facing", Config.data.show_facing));
        })
        .dimensions(centerX - 100, startY + spacing * 4, 200, 20)
        .build());

        // 6. Show Intercardinal Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Intercardinal Directions", Config.data.show_intercardinal), button -> {
            Config.data.show_intercardinal = !Config.data.show_intercardinal;
            Config.save();
            button.setMessage(getToggleText("Intercardinal Directions", Config.data.show_intercardinal));
        })
        .dimensions(centerX - 100, startY + spacing * 5, 200, 20)
        .build());

        // 7. Show Background Toggle
        this.addDrawableChild(ButtonWidget.builder(getToggleText("Show Background", Config.data.show_background), button -> {
            Config.data.show_background = !Config.data.show_background;
            Config.save();
            button.setMessage(getToggleText("Show Background", Config.data.show_background));
        })
        .dimensions(centerX - 100, startY + spacing * 6, 200, 20)
        .build());

        // 8. Precision Slider
        this.addDrawableChild(new SliderWidget(centerX - 100, startY + spacing * 7, 200, 20, Text.literal("Precision: " + Config.data.precision), (double) Config.data.precision / 5.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Decimal Precision: " + Config.data.precision));
            }

            @Override
            protected void applyValue() {
                Config.data.precision = (int) Math.round(this.value * 5.0);
                Config.save();
            }
        });

        // 9. HUD Editor Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Edit HUD Position (Drag & Drop)"), button -> {
            if (this.client != null) {
                this.client.setScreen(new HUDEditorScreen(this));
            }
        })
        .dimensions(centerX - 100, startY + spacing * 8, 200, 20)
        .build());

        // Back button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        })
        .dimensions(centerX - 100, this.height - 30, 200, 20)
        .build());
    }

    private Text getToggleText(String prefix, boolean value) {
        return Text.literal(prefix + ": " + (value ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTick) {
        super.render(drawContext, mouseX, mouseY, partialTick);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
