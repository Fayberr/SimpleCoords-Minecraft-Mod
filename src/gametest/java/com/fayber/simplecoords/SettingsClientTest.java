package com.fayber.simplecoords;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
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
        check(old.show_target && !old.show_target_type && !old.show_target_subchunk && !old.show_target_state, "target defaults migrate");
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
            c.runOnClient(mc -> {
                var pos = new net.minecraft.core.BlockPos(-17, -1, 16);
                var state = net.minecraft.world.level.block.Blocks.OAK_STAIRS.defaultBlockState();
                check(CoordsHUDOverlay.targetLines(pos, state).equals(java.util.List.of("Target XYZ: -17 / -1 / 16")), "coordinates only by default");
                Config.data.show_target_subchunk = true;
                Config.data.show_target_type = true;
                Config.data.show_target_state = true;
                var lines = CoordsHUDOverlay.targetLines(pos, state);
                check(lines.get(1).equals("Target Subchunk: 15 15 0"), "negative subchunk coordinates");
                check(lines.get(2).equals("Target Block: minecraft:oak_stairs"), "registry block type");
                check(lines.contains("  facing=north") && lines.contains("  waterlogged=false"), "actual state properties");
                mc.hitResult = net.minecraft.world.phys.BlockHitResult.miss(net.minecraft.world.phys.Vec3.ZERO, net.minecraft.core.Direction.UP, pos);
                check(CoordsHUDOverlay.targetLines().isEmpty(), "miss hides target");
                mc.hitResult = new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.ZERO, net.minecraft.core.Direction.UP, pos, false);
                check(!CoordsHUDOverlay.targetLines().isEmpty(), "block hit shows target");
            });
            command(c, "simplecoords set show_target false");
            c.runOnClient(mc -> {
                Config.load();
                check(!Config.data.show_target && Config.data.show_target_type && Config.data.show_target_state && Config.data.show_target_subchunk, "target preferences persist");
                check(CoordsHUDOverlay.targetLines().isEmpty(), "master toggle hides all target lines");
            });
            command(c, "simplecoords set show_biome false");
            check(!Config.data.show_biome, "biome command disables line");
            c.runOnClient(mc -> { Config.load(); check(!Config.data.show_biome, "biome preference persisted"); });
            command(c, "simplecoords config");
            boolean cloth = net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("cloth-config");
            c.runOnClient(mc -> {
                check(mc.screen != null, "command opened screen");
                check(cloth ? mc.screen.getClass().getName().equals("me.shedaniel.clothconfig2.gui.ClothConfigScreen") : mc.screen instanceof CustomConfigScreen, "settings routing");
                check(new SimpleCoordsModMenu().getModConfigScreenFactory().create(null).getClass()
                        .equals(mc.screen.getClass()), "ModMenu uses same router");
            });
            if (!cloth) {
                c.takeScreenshot("builtin-settings");
                c.runOnClient(mc -> {
                    var screen = mc.screen;
                    for (var child : screen.children()) {
                        if (child instanceof AbstractWidget widget) {
                            check(widget.getY() + widget.getHeight() <= screen.height, "settings control fits screen");
                        }
                    }
                });
                c.clickScreenButton("Target Coordinates: OFF");
                check(Config.data.show_target, "fallback target coordinates toggle");
                c.clickScreenButton("Target Coordinates: ON");
                check(!Config.data.show_target, "fallback target coordinates disables section");
                c.clickScreenButton("Show Biome: OFF");
                check(Config.data.show_biome, "fallback biome button enables line");
                c.clickScreenButton("Edit HUD Position");
                c.waitForScreen(HUDEditorScreen.class);
                c.takeScreenshot("editor-with-biome");
                c.runOnClient(mc -> mc.screen.onClose());
                c.runOnClient(mc -> mc.screen.onClose());
            } else {
                c.runOnClient(mc -> mc.screen.onClose());
            }
            world.getServer().runCommand("setblock 0 79 0 minecraft:oak_stairs[facing=east]");
            world.getServer().runCommand("tp @p 0.5 81 0.5 0 90");
            command(c, "simplecoords set show_target true");
            c.waitTicks(20);
            c.runOnClient(mc -> {
                var lines = CoordsHUDOverlay.targetLines();
                check(lines.contains("Target XYZ: 0 / 79 / 0"), "real crosshair block coordinates: " + lines);
                check(lines.contains("Target Block: minecraft:oak_stairs") && lines.contains("  facing=east"), "real crosshair state");
            });
            c.takeScreenshot("hud-with-target");
            world.getServer().runCommand("tp @p 0.5 81 0.5 0 -90");
            c.waitTicks(10);
            c.runOnClient(mc -> check(CoordsHUDOverlay.targetLines().isEmpty(), "looking into sky clears target"));
            System.out.println("[SimpleCoords tests] biome lookup, config migration/persistence, screen routing and settings passed");
        }
    }
}
