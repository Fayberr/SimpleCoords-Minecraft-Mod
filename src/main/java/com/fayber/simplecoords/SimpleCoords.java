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

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            // LOGGER.info("HudRenderCallback fired"); // Too much spam, but good for local check
            CoordsHUDOverlay.render(drawContext, tickCounter);
        });
        
        ClientCommandHandler.register();

        LOGGER.info("SimpleCoordsHUD v1.1.1 initialized! HUD Enabled: {}", Config.data.enabled);
    }
}
