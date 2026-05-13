package com.fayber.simplecoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCoords implements ClientModInitializer {
    public static final String MODID = "simplecoords";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeClient() {
        Config.load();
        
        HudRenderCallback.EVENT.register(CoordsHUDOverlay::render);
        ClientCommandHandler.register();
        
        LOGGER.info("SimpleCoordsHUD initialized!");
    }
}
