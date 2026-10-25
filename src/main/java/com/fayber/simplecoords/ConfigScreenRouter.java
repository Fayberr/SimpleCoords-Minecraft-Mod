package com.fayber.simplecoords;

import net.minecraft.client.gui.screen.Screen;
import net.fabricmc.loader.api.FabricLoader;

public final class ConfigScreenRouter {
    private ConfigScreenRouter() {}

    public static Screen create(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ClothConfigIntegration.create(parent);
        }
        return new CustomConfigScreen(parent);
    }
}
