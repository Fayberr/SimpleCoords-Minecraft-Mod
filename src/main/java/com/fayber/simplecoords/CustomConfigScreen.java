package com.fayber.simplecoords;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CustomConfigScreen extends Screen {
    private final Screen parent;

    public CustomConfigScreen(Screen parent) {
        super(Component.literal("SimpleCoords Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 23;
        int spacing = 22;

        this.addRenderableWidget(Button.builder(getToggleText("HUD Enabled", Config.data.enabled), button -> {
            Config.data.enabled = !Config.data.enabled;
            Config.save();
            button.setMessage(getToggleText("HUD Enabled", Config.data.enabled));
        })
        .bounds(centerX - 155, startY, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Use Camera Pos", Config.data.use_camera), button -> {
            Config.data.use_camera = !Config.data.use_camera;
            Config.save();
            button.setMessage(getToggleText("Use Camera Pos", Config.data.use_camera));
        })
        .bounds(centerX + 5, startY, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Show XYZ", Config.data.show_xyz), button -> {
            Config.data.show_xyz = !Config.data.show_xyz;
            Config.save();
            button.setMessage(getToggleText("Show XYZ", Config.data.show_xyz));
        })
        .bounds(centerX - 155, startY + spacing * 1, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Show Subchunk", Config.data.show_subchunk), button -> {
            Config.data.show_subchunk = !Config.data.show_subchunk;
            Config.save();
            button.setMessage(getToggleText("Show Subchunk", Config.data.show_subchunk));
        })
        .bounds(centerX + 5, startY + spacing * 1, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Show Facing", Config.data.show_facing), button -> {
            Config.data.show_facing = !Config.data.show_facing;
            Config.save();
            button.setMessage(getToggleText("Show Facing", Config.data.show_facing));
        })
        .bounds(centerX - 155, startY + spacing * 2, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Intercardinal", Config.data.show_intercardinal), button -> {
            Config.data.show_intercardinal = !Config.data.show_intercardinal;
            Config.save();
            button.setMessage(getToggleText("Intercardinal", Config.data.show_intercardinal));
        })
        .bounds(centerX + 5, startY + spacing * 2, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Show Background", Config.data.show_background), button -> {
            Config.data.show_background = !Config.data.show_background;
            Config.save();
            button.setMessage(getToggleText("Show Background", Config.data.show_background));
        })
        .bounds(centerX - 155, startY + spacing * 3, 150, 20)
        .build());

        this.addRenderableWidget(new AbstractSliderButton(centerX + 5, startY + spacing * 3, 150, 20, Component.literal("Precision: " + Config.data.precision), (double) Config.data.precision / 5.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Decimal Precision: " + Config.data.precision));
            }

            @Override
            protected void applyValue() {
                Config.data.precision = (int) Math.round(this.value * 5.0);
                Config.save();
            }
        });

        this.addRenderableWidget(Button.builder(Component.literal("Edit HUD Position"), button -> {
            this.minecraft.setScreenAndShow(new HUDEditorScreen(this));
        })
        .bounds(centerX - 155, startY + spacing * 4, 150, 20)
        .build());

        this.addRenderableWidget(Button.builder(getToggleText("Show Biome", Config.data.show_biome), button -> {
            Config.data.show_biome = !Config.data.show_biome;
            Config.save();
            button.setMessage(getToggleText("Show Biome", Config.data.show_biome));
        }).bounds(centerX + 5, startY + spacing * 4, 150, 20).build());

        addTargetToggle("Target Coordinates", () -> Config.data.show_target, v -> Config.data.show_target = v, centerX - 155, startY + spacing * 5);
        addTargetToggle("Target Subchunk", () -> Config.data.show_target_subchunk, v -> Config.data.show_target_subchunk = v, centerX + 5, startY + spacing * 5);
        addTargetToggle("Target Block Type", () -> Config.data.show_target_type, v -> Config.data.show_target_type = v, centerX - 155, startY + spacing * 6);
        addTargetToggle("Target Block State", () -> Config.data.show_target_state, v -> Config.data.show_target_state = v, centerX + 5, startY + spacing * 6);

        // Back button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> {
            this.minecraft.setScreenAndShow(this.parent);
        })
        .bounds(centerX - 100, this.height - 30, 200, 20)
        .build());
    }

    private void addTargetToggle(String label, java.util.function.BooleanSupplier get,
                                 java.util.function.Consumer<Boolean> set, int x, int y) {
        addRenderableWidget(Button.builder(getToggleText(label, get.getAsBoolean()), button -> {
            set.accept(!get.getAsBoolean());
            Config.save();
            button.setMessage(getToggleText(label, get.getAsBoolean()));
        }).bounds(x, y, 150, 20).build());
    }

    private Component getToggleText(String prefix, boolean value) {
        return Component.literal(prefix + ": " + (value ? "ON" : "OFF"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }
}
