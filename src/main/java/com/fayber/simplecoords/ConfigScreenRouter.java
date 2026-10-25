package com.fayber.simplecoords;

import net.minecraft.client.gui.screens.Screen;

public final class ConfigScreenRouter {
    private ConfigScreenRouter() {}

    public static Screen create(Screen parent) {
        return new CustomConfigScreen(parent);
    }
}
