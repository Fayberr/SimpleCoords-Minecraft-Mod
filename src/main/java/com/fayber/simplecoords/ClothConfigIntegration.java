package com.fayber.simplecoords;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// Loaded only after the router finds Cloth Config.
public final class ClothConfigIntegration {
    private ClothConfigIntegration() {}

    public static Screen create(Screen parent) {
        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("SimpleCoords Settings"))
                .setSavingRunnable(Config.SPEC::save);
        var entries = builder.entryBuilder();
        var hud = builder.getOrCreateCategory(Component.literal("HUD"));
        hud.addEntry(entries.startBooleanToggle(Component.literal("HUD Enabled"), Config.ENABLED.get())
                .setDefaultValue(Config.ENABLED.getDefault()).setSaveConsumer(Config.ENABLED::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Use Camera Position"), Config.USE_CAMERA.get())
                .setDefaultValue(Config.USE_CAMERA.getDefault()).setSaveConsumer(Config.USE_CAMERA::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show XYZ"), Config.SHOW_XYZ.get())
                .setDefaultValue(Config.SHOW_XYZ.getDefault()).setSaveConsumer(Config.SHOW_XYZ::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Subchunk"), Config.SHOW_SUBCHUNK.get())
                .setDefaultValue(Config.SHOW_SUBCHUNK.getDefault()).setSaveConsumer(Config.SHOW_SUBCHUNK::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Facing"), Config.SHOW_FACING.get())
                .setDefaultValue(Config.SHOW_FACING.getDefault()).setSaveConsumer(Config.SHOW_FACING::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Intercardinal Directions"), Config.SHOW_INTERCARDINAL.get())
                .setDefaultValue(Config.SHOW_INTERCARDINAL.getDefault()).setSaveConsumer(Config.SHOW_INTERCARDINAL::set).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Background"), Config.SHOW_BACKGROUND.get())
                .setDefaultValue(Config.SHOW_BACKGROUND.getDefault()).setSaveConsumer(Config.SHOW_BACKGROUND::set).build());
        hud.addEntry(entries.startIntSlider(Component.literal("Decimal Precision"), Config.COORD_PRECISION.get(), 0, 5)
                .setDefaultValue(Config.COORD_PRECISION.getDefault())
                .setSaveConsumer(Config.COORD_PRECISION::set).build());
        var position = builder.getOrCreateCategory(Component.literal("Position"));
        position.addEntry(entries.startIntField(Component.literal("HUD X"), Config.HUD_X.get())
                .setMin(0).setMax(4000).setDefaultValue(Config.HUD_X.getDefault())
                .setSaveConsumer(Config.HUD_X::set).build());
        position.addEntry(entries.startIntField(Component.literal("HUD Y"), Config.HUD_Y.get())
                .setMin(0).setMax(4000).setDefaultValue(Config.HUD_Y.getDefault())
                .setSaveConsumer(Config.HUD_Y::set).build());
        return builder.build();
    }
}
