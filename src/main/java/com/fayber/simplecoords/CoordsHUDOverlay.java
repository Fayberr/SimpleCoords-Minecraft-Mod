package com.fayber.simplecoords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class CoordsHUDOverlay {

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Config.ENABLED.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        Entity target = Config.USE_CAMERA.get() && mc.getCameraEntity() != null ? mc.getCameraEntity() : mc.player;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);

        double x = Mth.lerp((double)partialTick, target.xo, target.getX());
        double y = Mth.lerp((double)partialTick, target.yo, target.getY());
        double z = Mth.lerp((double)partialTick, target.zo, target.getZ());

        float yaw = target.getViewYRot(partialTick);
        String facing = getFacingDirection(yaw, Config.SHOW_INTERCARDINAL.get());

        renderHUD(guiGraphics, x, y, z, facing, Config.HUD_X.get(), Config.HUD_Y.get());
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

    public static void renderHUD(GuiGraphics guiGraphics, double x, double y, double z, String facing, int xPos, int yPos) {
        Minecraft mc = Minecraft.getInstance();
        int textColor = 0xFFFFFFFF; // White
        int lineHeight = 10;

        List<String> lines = new ArrayList<>();

        if (Config.SHOW_XYZ.get()) {
            int precision = Config.COORD_PRECISION.get();
            String coordsText;
            if (precision == 0) {
                coordsText = String.format("XYZ: %d / %d / %d", Mth.floor(x), Mth.floor(y), Mth.floor(z));
            } else {
                String format = "%." + precision + "f";
                coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            }
            lines.add(coordsText);
        }

        if (Config.SHOW_SUBCHUNK.get()) {
            int subX = Mth.floor(x) & 15;
            int subY = Mth.floor(y) & 15;
            int subZ = Mth.floor(z) & 15;
            lines.add(String.format("Subchunk: %d %d %d", subX, subY, subZ));
        }

        if (Config.SHOW_FACING.get()) {
            lines.add("Facing: " + facing);
        }

        if (lines.isEmpty()) return;

        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, mc.font.width(line));
        }

        if (Config.SHOW_BACKGROUND.get()) {
            guiGraphics.fill(xPos - 2, yPos - 2, xPos + maxWidth + 2, yPos + (lines.size() * lineHeight), 0x80000000);
        }

        int currentY = yPos;
        for (String line : lines) {
            guiGraphics.drawString(mc.font, line, xPos, currentY, textColor);
            currentY += lineHeight;
        }
    }
}
