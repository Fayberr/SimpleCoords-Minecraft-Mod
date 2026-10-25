package com.fayber.simplecoords.test;

import com.fayber.simplecoords.Config;
import com.fayber.simplecoords.CustomConfigScreen;
import com.fayber.simplecoords.ConfigScreenRouter;
import com.fayber.simplecoords.SimpleCoords;

import java.nio.file.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@Mod(value = "simplecoords_settings_test", dist = Dist.CLIENT)
public class SettingsClientTest {
    private int ticks;
    private int phase;
    private Screen parent;

    public SettingsClientTest() {
        NeoForge.EVENT_BUS.addListener(this::tick);
    }

    private void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getOverlay() != null || ++ticks < 10) return;
        try {
            boolean cloth = ModList.get().isLoaded("cloth_config");
            if (phase == 0) {
                if (mc.screen instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen) {
                    mc.setScreen(new TitleScreen());
                    return;
                }
                if (!(mc.screen instanceof TitleScreen)) return;
                parent = mc.screen;
                Config.ENABLED.set(true);
                Config.ENABLED.save();
                var screen = ConfigScreenRouter.create(ModList.get().getModContainerById(SimpleCoords.MODID).orElseThrow(), parent);
                String expected = cloth ? "me.shedaniel.clothconfig2.gui.ClothConfigScreen" : CustomConfigScreen.class.getName();
                if (!screen.getClass().getName().equals(expected)) throw new AssertionError("settings route: " + screen.getClass());
                mc.setScreen(screen);
                phase = 1;
                ticks = 0;
            } else if (phase == 1) {
                if (cloth) NeoClothSettingsChecks.save(mc.screen);
                mc.setScreen(parent);
                phase = 2;
                ticks = 0;
            } else {
                if (cloth && !Files.readString(FMLPaths.CONFIGDIR.get().resolve("simplecoords-client.toml")).contains("enabled = false")) {
                    throw new AssertionError("Cloth Save did not persist the NeoForge config");
                }
                System.out.println("[SimpleCoords NeoForge settings] routing, settings coverage and persistence passed; cloth=" + cloth);
                phase = 3;
                mc.getSoundManager().destroy();
                mc.stop();
            }
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }
}
