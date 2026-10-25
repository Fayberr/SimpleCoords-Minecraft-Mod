package com.fayber.simplecoords;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

public class SettingsRoutingClientTest implements FabricClientGameTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    @Override
    public void runTest(ClientGameTestContext c) {
        boolean modern = FabricLoader.getInstance().isModLoaded("modernconfig");
        boolean cloth = FabricLoader.getInstance().isModLoaded("cloth-config");
        Screen[] parent = new Screen[1];
        c.runOnClient(mc -> {
            Config.data = new Config.Data();
            Config.save();
            parent[0] = mc.gui.screen();
            Screen screen = ConfigScreenRouter.create(parent[0]);
            String expected = modern ? "net.fayber.modernconfig.api.ModernConfigScreen"
                    : cloth ? "me.shedaniel.clothconfig2.gui.ClothConfigScreen" : CustomConfigScreen.class.getName();
            check(screen.getClass().getName().equals(expected), "settings priority: " + screen.getClass().getName());
            check(new SimpleCoordsModMenu().getModConfigScreenFactory().create(parent[0]).getClass() == screen.getClass(), "ModMenu shares settings route");
            mc.setScreenAndShow(screen);
        });
        c.waitTicks(5);
        c.takeScreenshot(modern ? "modern-routing" : cloth ? "cloth-routing" : "builtin-routing");
        if (modern) {
            c.runOnClient(mc -> {
                Config.data.enabled = false;
                mc.gui.screen().onClose();
                check(Config.data.enabled, "Modern Config Cancel restores edits");
                mc.setScreenAndShow(ConfigScreenRouter.create(parent[0]));
                Config.data.enabled = false;
            });
            c.waitTicks(3);
            int[] xy = c.computeOnClient(mc -> {
                var button = mc.gui.screen().children().stream()
                        .filter(child -> child instanceof net.minecraft.client.gui.components.AbstractWidget widget && widget.getMessage().getString().equals("Save"))
                        .map(child -> (net.minecraft.client.gui.components.AbstractWidget) child).findFirst().orElseThrow();
                return new int[] {
                    (button.getX() + button.getWidth() / 2) * mc.getWindow().getWidth() / mc.getWindow().getGuiScaledWidth(),
                    (button.getY() + button.getHeight() / 2) * mc.getWindow().getHeight() / mc.getWindow().getGuiScaledHeight()
                };
            });
            c.getInput().setCursorPos(xy[0], xy[1]);
            c.getInput().pressMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT);
            c.waitTicks(3);
            c.runOnClient(mc -> {
                check(mc.gui.screen() == parent[0], "Modern Config Save returns to parent");
                Config.load();
                check(!Config.data.enabled, "Modern Config Save persists edits");
            });
        } else if (cloth) {
            ClothSettingsClientChecks.run(c, parent[0]);
        } else {
            c.runOnClient(mc -> mc.gui.screen().onClose());
        }
        // Release audio before the test runner exits the JVM.
        c.runOnClient(mc -> mc.getSoundManager().destroy());
        System.out.println("[SimpleCoords routing] passed; modern=" + modern + "; cloth=" + cloth);
    }
}
