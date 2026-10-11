package com.fayber.simplecoords;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.InputConstants;
import java.nio.file.Files;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.AbstractWidget;

public class SettingsClientTest implements FabricClientGameTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void command(ClientGameTestContext c, String command) {
        c.runOnClient(mc -> mc.getConnection().sendCommand(command));
        c.waitTicks(5);
    }

    @Override
    public void runTest(ClientGameTestContext c) {
        // Existing configs must acquire the new default while preserving their old options.
        Config.Data old = new Gson().fromJson("{\"show_xyz\":false,\"hud_x\":42}", Config.Data.class);
        check(old.show_biome && !old.show_xyz && old.hud_x == 42, "old config migration");
        c.runOnClient(mc -> { Config.data = new Config.Data(); Config.save(); });
        try (var world = c.worldBuilder().create()) {
            c.waitTicks(40);
            world.getServer().runCommand("gamemode creative @p");
            world.getServer().runCommand("tp @p 0 80 0");
            c.waitTicks(20);
            world.getServer().runCommand("fillbiome -4 64 -4 4 96 4 minecraft:desert");
            c.waitTicks(20);
            c.runOnClient(mc -> {
                check(CoordsHUDOverlay.biomeAt(0, 80, 0).equals("Desert"), "biome lookup at player position: " + CoordsHUDOverlay.biomeAt(0, 80, 0));
                var key = mc.level.getBiome(net.minecraft.core.BlockPos.containing(0, 80, 0)).unwrapKey().orElseThrow();
                check(key.identifier().getPath().equals("desert"), "test biome update reached client");
            });
            world.getServer().runCommand("fillbiome -4 64 -4 4 96 4 minecraft:snowy_plains");
            c.waitTicks(20);
            c.runOnClient(mc -> check(CoordsHUDOverlay.biomeAt(0, 80, 0).equals("Snowy Plains"), "biome changes and readable names"));
            command(c, "simplecoords set show_biome false");
            check(!Config.data.show_biome, "biome command disables line");
            c.runOnClient(mc -> { Config.load(); check(!Config.data.show_biome, "biome preference persisted"); });
            command(c, "simplecoords config");
            boolean modern = FabricLoader.getInstance().isModLoaded("modernconfig");
            c.runOnClient(mc -> {
                check(mc.gui.screen() != null, "command opened screen");
                check(mc.gui.screen().getClass().getName().equals(modern
                        ? "net.fayber.modernconfig.api.ModernConfigScreen"
                        : CustomConfigScreen.class.getName()), "optional screen routing");
                check(new SimpleCoordsModMenu().getModConfigScreenFactory().create(null).getClass()
                        .equals(mc.gui.screen().getClass()), "ModMenu uses same router");
            });
            c.takeScreenshot(modern ? "modern-settings" : "builtin-settings");
            if (!modern) {
                c.runOnClient(mc -> {
                    var screen = mc.gui.screen();
                    for (var child : screen.children()) {
                        if (child instanceof AbstractWidget widget) {
                            check(widget.getY() + widget.getHeight() <= screen.height, "settings control fits screen");
                        }
                    }
                });
                c.clickScreenButton("Show Biome: OFF");
                check(Config.data.show_biome, "fallback biome button enables line");
                c.clickScreenButton("Edit HUD Position");
                c.waitForScreen(HUDEditorScreen.class);
                c.takeScreenshot("editor-with-biome");
                c.runOnClient(mc -> mc.gui.screen().onClose());
                c.runOnClient(mc -> mc.gui.screen().onClose());
            } else {
                // Exercise the actual screen's snapshot/Cancel and Save persistence paths.
                c.runOnClient(mc -> { Config.data.show_biome = true; mc.gui.screen().onClose(); });
                check(!Config.data.show_biome, "Modern Config cancel restores biome preference");
                command(c, "simplecoords");
                c.runOnClient(mc -> Config.data.show_biome = true);
                // Fabric's named-button helper handles vanilla Buttons only; Modern Config uses FlatButton.
                int[] save = new int[2];
                c.runOnClient(mc -> {
                    var button = mc.gui.screen().children().stream()
                            .filter(child -> child instanceof AbstractWidget widget && widget.getMessage().getString().equals("Save"))
                            .map(child -> (AbstractWidget) child).findFirst().orElseThrow();
                    save[0] = (button.getX() + button.getWidth() / 2) * mc.getWindow().getWidth() / mc.getWindow().getGuiScaledWidth();
                    save[1] = (button.getY() + button.getHeight() / 2) * mc.getWindow().getHeight() / mc.getWindow().getGuiScaledHeight();
                });
                c.getInput().setCursorPos(save[0], save[1]);
                c.waitTicks(2);
                c.getInput().holdMouse(InputConstants.MOUSE_BUTTON_LEFT);
                c.waitTicks(2);
                c.getInput().releaseMouse(InputConstants.MOUSE_BUTTON_LEFT);
                c.waitTicks(2);
                c.runOnClient(mc -> { check(mc.gui.screen() == null, "Save click closed the screen"); Config.load(); check(Config.data.show_biome, "Modern Config save persists biome preference"); });
            }
            c.takeScreenshot("hud-with-biome");
            System.out.println("[SimpleCoords tests] biome lookup, config migration/persistence, screen routing and settings passed; modern=" + modern);
        }
    }
}
