package com.fayber.simplecoords;

import net.fayber.modernconfig.api.ModernConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// Loaded only after the router checks for Modern Config.
public final class ModernConfigIntegration {
    private ModernConfigIntegration() {}

    public static Screen create(Screen parent) {
        Config.Data c = Config.data;
        var window = Minecraft.getInstance().getWindow();
        return ModernConfigScreen.builder(Component.literal("SimpleCoords Settings"), parent, Config::save)
                .category("HUD")
                .bool("HUD Enabled", () -> c.enabled, v -> c.enabled = v, true)
                .bool("Use Camera Position", () -> c.use_camera, v -> c.use_camera = v, true)
                .bool("Show XYZ", () -> c.show_xyz, v -> c.show_xyz = v, true)
                .bool("Show Subchunk", () -> c.show_subchunk, v -> c.show_subchunk = v, true)
                .bool("Show Facing", () -> c.show_facing, v -> c.show_facing = v, true)
                .bool("Show Biome", () -> c.show_biome, v -> c.show_biome = v, true)
                .bool("Intercardinal Directions", () -> c.show_intercardinal, v -> c.show_intercardinal = v, false)
                .bool("Show Background", () -> c.show_background, v -> c.show_background = v, true)
                .intSlider("Decimal Precision", () -> c.precision, v -> c.precision = v, 0, 5, 1, 0)
                .category("Position")
                .intSlider("HUD X", () -> c.hud_x, v -> c.hud_x = v, 0, Math.max(c.hud_x, window.getGuiScaledWidth()), 1, 5)
                .intSlider("HUD Y", () -> c.hud_y, v -> c.hud_y = v, 0, Math.max(c.hud_y, window.getGuiScaledHeight()), 1, 5)
                .build();
    }
}
