package com.fayber.simplecoords;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;

public final class ConfigScreenRouter {
    private ConfigScreenRouter() {}

    public static Screen create(ModContainer container, Screen parent) {
        if (ModList.get().isLoaded("cloth_config")) {
            return ClothConfigIntegration.create(parent);
        }
        return new CustomConfigScreen(container, parent);
    }
}
