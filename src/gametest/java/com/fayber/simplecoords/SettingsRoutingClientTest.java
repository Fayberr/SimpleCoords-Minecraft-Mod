package com.fayber.simplecoords;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

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
            parent[0] = mc.currentScreen;
            Screen screen = ConfigScreenRouter.create(parent[0]);
            String expected = modern ? "net.fayber.modernconfig.api.ModernConfigScreen"
                    : cloth ? "me.shedaniel.clothconfig2.gui.ClothConfigScreen" : CustomConfigScreen.class.getName();
            check(screen.getClass().getName().equals(expected), "settings priority: " + screen.getClass().getName());
            check(new SimpleCoordsModMenu().getModConfigScreenFactory().create(parent[0]).getClass() == screen.getClass(), "ModMenu shares settings route");
            mc.setScreen(screen);
        });
        c.waitTicks(5);
        c.takeScreenshot(modern ? "modern-routing" : cloth ? "cloth-routing" : "builtin-routing");
        if (modern) {
            c.runOnClient(mc -> {
                Config.data.enabled = false;
                mc.currentScreen.close();
                check(Config.data.enabled, "Modern Config Cancel restores edits");
                mc.setScreen(ConfigScreenRouter.create(parent[0]));
                Config.data.enabled = false;
            });
            c.waitTicks(3);
            int[] xy = c.computeOnClient(mc -> {
                var button = mc.currentScreen.children().stream()
                        .filter(child -> child instanceof net.minecraft.client.gui.widget.ClickableWidget widget && widget.getMessage().getString().equals("Save"))
                        .map(child -> (net.minecraft.client.gui.widget.ClickableWidget) child).findFirst().orElseThrow();
                return new int[] {
                    (button.getX() + button.getWidth() / 2) * mc.getWindow().getWidth() / mc.getWindow().getScaledWidth(),
                    (button.getY() + button.getHeight() / 2) * mc.getWindow().getHeight() / mc.getWindow().getScaledHeight()
                };
            });
            c.getInput().setCursorPos(xy[0], xy[1]);
            c.getInput().pressMouse(0);
            c.waitTicks(3);
            c.runOnClient(mc -> {
                check(mc.currentScreen == parent[0], "Modern Config Save returns to parent");
                Config.load();
                check(!Config.data.enabled, "Modern Config Save persists edits");
            });
        } else if (cloth) {
            ClothSettingsClientChecks.run(c, parent[0]);
        } else {
            c.runOnClient(mc -> mc.currentScreen.close());
        }
        // Release audio before the test runner exits the JVM.
        c.runOnClient(mc -> mc.getSoundManager().close());
        System.out.println("[SimpleCoords routing] passed; modern=" + modern + "; cloth=" + cloth);
    }
}
