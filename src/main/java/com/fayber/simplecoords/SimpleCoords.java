package com.fayber.simplecoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCoords implements ClientModInitializer {
    public static final String MODID = "simplecoords";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeClient() {
        Config.load();

        // 26.1.2 HUD API: HudRenderCallback was replaced by HudElementRegistry.
        // Attach after subtitles so the HUD renders above most vanilla layers,
        // while still inheriting the vanilla hide-gui render condition.
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.SUBTITLES,
                Identifier.withDefaultNamespace("simplecoords_hud"),
                (graphics, deltaTracker) -> CoordsHUDOverlay.render(graphics, deltaTracker)
        );

        ClientCommandHandler.register();

        LOGGER.info("SimpleCoordsHUD initialized. HUD enabled: {}", Config.data.enabled);
    }
}
