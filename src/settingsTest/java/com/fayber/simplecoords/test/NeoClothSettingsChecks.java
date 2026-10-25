package com.fayber.simplecoords.test;

import com.fayber.simplecoords.Config;
import com.fayber.simplecoords.ConfigScreenRouter;
import com.fayber.simplecoords.SimpleCoords;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

final class NeoClothSettingsChecks {
    static void save(Screen current) {
        var screen = (ClothConfigScreen) current;
        var entries = screen.getCategorizedEntries().values().stream().flatMap(java.util.List::stream).toList();
        if (entries.size() != 10) throw new AssertionError("Cloth must expose all ten NeoForge settings");
        var enabled = entries.stream().filter(e -> e instanceof BooleanListEntry && e.getFieldName().getString().equals("HUD Enabled"))
                .map(e -> (BooleanListEntry) e).findFirst().orElseThrow();
        var button = enabled.children().stream().filter(child -> child instanceof Button b && b.getWidth() > 50)
                .map(child -> (Button) child).findFirst().orElseThrow();
        button.onPress();
        if (enabled.getValue() || !Config.ENABLED.get()) throw new AssertionError("Cloth toggle must stage its value");
        screen.saveAll(false);
        if (Config.ENABLED.get()) throw new AssertionError("Cloth Save must apply its value");
    }
}
