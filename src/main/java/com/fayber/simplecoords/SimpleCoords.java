package com.fayber.simplecoords;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.resources.ResourceLocation;

@Mod(SimpleCoords.MODID)
public class SimpleCoords {
    public static final String MODID = "simplecoords";

    public SimpleCoords(IEventBus modEventBus, ModContainer modContainer) {
        // Register the configuration
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        // Register the CUSTOM config screen
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent) -> new CustomConfigScreen(modContainer, parent));

        // Register the HUD layer
        modEventBus.addListener(this::registerGuiLayers);
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(MODID, "coords_hud"), CoordsHUDOverlay::render);
    }
}
