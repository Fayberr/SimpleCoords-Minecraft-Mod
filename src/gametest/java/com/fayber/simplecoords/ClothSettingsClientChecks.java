package com.fayber.simplecoords;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

final class ClothSettingsClientChecks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    static void run(ClientGameTestContext c, Screen parent) {
        int[] xy = c.computeOnClient(mc -> {
            var screen = (ClothConfigScreen) mc.screen;
            var entries = screen.getCategorizedEntries().values().stream().flatMap(java.util.List::stream).toList();
            check(entries.size() == Config.Data.class.getDeclaredFields().length, "Cloth exposes every setting");
            var enabled = entries.stream().filter(e -> e instanceof BooleanListEntry && e.getFieldName().getString().equals("HUD Enabled")).findFirst().orElseThrow();
            var button = enabled.children().stream().filter(child -> child instanceof AbstractWidget w && w.getWidth() > 50)
                    .map(child -> (AbstractWidget) child).findFirst().orElseThrow();
            return new int[] {
                (button.getX() + button.getWidth() / 2) * mc.getWindow().getWidth() / mc.getWindow().getGuiScaledWidth(),
                (button.getY() + button.getHeight() / 2) * mc.getWindow().getHeight() / mc.getWindow().getGuiScaledHeight()
            };
        });
        c.getInput().setCursorPos(xy[0], xy[1]);
        c.getInput().pressMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT);
        c.waitTicks(3);
        c.runOnClient(mc -> {
            var screen = (ClothConfigScreen) mc.screen;
            var enabled = screen.getCategorizedEntries().values().stream().flatMap(java.util.List::stream)
                    .filter(e -> e instanceof BooleanListEntry && e.getFieldName().getString().equals("HUD Enabled"))
                    .map(e -> (BooleanListEntry) e).findFirst().orElseThrow();
            check(!enabled.getValue(), "Cloth toggle receives mouse input");
            check(Config.data.enabled, "Cloth edits remain staged before Save");
            screen.saveAll(true);
            mc.setScreen(parent);
            Config.load();
            check(!Config.data.enabled, "Cloth Save persists edits");
        });
    }
}
