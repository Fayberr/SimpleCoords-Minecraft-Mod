package com.fayber.simplecoords;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class CoordsHUDOverlay {
    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();

        // The element is attached after SUBTITLES, so the vanilla hide-gui
        // render condition is inherited (26.2 removed Options.hideGui).
        if (!Config.data.enabled || client.player == null) {
            return;
        }

        Entity target = Config.data.use_camera && client.getCameraEntity() != null ? client.getCameraEntity() : client.player;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);

        double x = Mth.lerp((double) partialTick, target.xo, target.getX());
        double y = Mth.lerp((double) partialTick, target.yo, target.getY());
        double z = Mth.lerp((double) partialTick, target.zo, target.getZ());

        float yaw = target.getViewYRot(partialTick);
        String facing = getFacingDirection(yaw, Config.data.show_intercardinal);

        renderHUD(graphics, x, y, z, facing, Config.data.hud_x, Config.data.hud_y);
    }

    private static String getFacingDirection(float yaw, boolean includeIntercardinal) {
        float degree = Mth.wrapDegrees(yaw);

        if (includeIntercardinal) {
            if (degree >= -22.5 && degree < 22.5) return "SOUTH";
            if (degree >= 22.5 && degree < 67.5) return "SOUTH-WEST";
            if (degree >= 67.5 && degree < 112.5) return "WEST";
            if (degree >= 112.5 && degree < 157.5) return "NORTH-WEST";
            if (degree >= 157.5 || degree < -157.5) return "NORTH";
            if (degree >= -157.5 && degree < -112.5) return "NORTH-EAST";
            if (degree >= -112.5 && degree < -67.5) return "EAST";
            if (degree >= -67.5 && degree < -22.5) return "SOUTH-EAST";
        } else {
            if (degree >= -45 && degree < 45) return "SOUTH";
            if (degree >= 45 && degree < 135) return "WEST";
            if (degree >= 135 || degree < -135) return "NORTH";
            if (degree >= -135 && degree < -45) return "EAST";
        }
        return "NORTH";
    }

    public static void renderHUD(GuiGraphicsExtractor graphics, double x, double y, double z, String facing, int xPos, int yPos) {
        Minecraft client = Minecraft.getInstance();
        int textColor = 0xFFFFFFFF; // White
        int lineHeight = 10;

        List<String> lines = new ArrayList<>();

        if (Config.data.show_xyz) {
            int precision = Config.data.precision;
            String coordsText;
            if (precision == 0) {
                coordsText = String.format("XYZ: %d / %d / %d", Mth.floor(x), Mth.floor(y), Mth.floor(z));
            } else {
                String format = "%." + precision + "f";
                coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            }
            lines.add(coordsText);
        }

        if (Config.data.show_subchunk) {
            int subX = Mth.floor(x) & 15;
            int subY = Mth.floor(y) & 15;
            int subZ = Mth.floor(z) & 15;
            lines.add(String.format("Subchunk: %d %d %d", subX, subY, subZ));
        }

        if (Config.data.show_facing) {
            lines.add("Facing: " + facing);
        }

        if (lines.isEmpty()) return;

        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, client.font.width(line));
        }

        if (Config.data.show_background) {
            graphics.fill(xPos - 2, yPos - 2, xPos + maxWidth + 2, yPos + (lines.size() * lineHeight), 0x80000000);
        }

        int currentY = yPos;
        for (String line : lines) {
            graphics.text(client.font, line, xPos, currentY, textColor, true);
            currentY += lineHeight;
        }
    }
}
