package com.fayber.simplecoords;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// Loaded only after the router finds Cloth Config.
public final class ClothConfigIntegration {
    private ClothConfigIntegration() {}

    public static Screen create(Screen parent) {
        Config.Data c = Config.data;
        Config.Data defaults = new Config.Data();
        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("SimpleCoords Settings"))
                .setSavingRunnable(Config::save);
        var entries = builder.entryBuilder();
        var hud = builder.getOrCreateCategory(Component.literal("HUD"));
        hud.addEntry(entries.startBooleanToggle(Component.literal("HUD Enabled"), c.enabled)
                .setDefaultValue(defaults.enabled).setSaveConsumer(v -> c.enabled = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Use Camera Position"), c.use_camera)
                .setDefaultValue(defaults.use_camera).setSaveConsumer(v -> c.use_camera = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show XYZ"), c.show_xyz)
                .setDefaultValue(defaults.show_xyz).setSaveConsumer(v -> c.show_xyz = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Subchunk"), c.show_subchunk)
                .setDefaultValue(defaults.show_subchunk).setSaveConsumer(v -> c.show_subchunk = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Facing"), c.show_facing)
                .setDefaultValue(defaults.show_facing).setSaveConsumer(v -> c.show_facing = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Intercardinal Directions"), c.show_intercardinal)
                .setDefaultValue(defaults.show_intercardinal).setSaveConsumer(v -> c.show_intercardinal = v).build());
        hud.addEntry(entries.startBooleanToggle(Component.literal("Show Background"), c.show_background)
                .setDefaultValue(defaults.show_background).setSaveConsumer(v -> c.show_background = v).build());
        hud.addEntry(entries.startIntSlider(Component.literal("Decimal Precision"), c.precision, 0, 5)
                .setDefaultValue(defaults.precision)
                .setSaveConsumer(v -> c.precision = v).build());
        var position = builder.getOrCreateCategory(Component.literal("Position"));
        position.addEntry(entries.startIntField(Component.literal("HUD X"), c.hud_x)
                .setMin(0).setDefaultValue(defaults.hud_x)
                .setSaveConsumer(v -> c.hud_x = v).build());
        position.addEntry(entries.startIntField(Component.literal("HUD Y"), c.hud_y)
                .setMin(0).setDefaultValue(defaults.hud_y)
                .setSaveConsumer(v -> c.hud_y = v).build());
        return builder.build();
    }
}
