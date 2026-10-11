package com.fayber.simplecoords;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

public final class ConfigScreenRouter {
    private ConfigScreenRouter() {}

    public static Screen create(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("modernconfig")) {
            return ModernConfigIntegration.create(parent);
        }
        return new CustomConfigScreen(parent);
    }
}
