package com.fayber.simplecoords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.Entity;

public class CoordsHUDOverlay {

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Config.ENABLED.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        // Use the camera entity (e.g., the Freecam ghost) if enabled, otherwise use the player.
        // We use the entity's position directly (feet level) to match the F3 screen's XYZ line.
        Entity target = (Config.USE_CAMERA.get() && mc.getCameraEntity() != null) ? mc.getCameraEntity() : mc.player;

        renderHUD(guiGraphics, target.getX(), target.getY(), target.getZ(), target.getDirection().getName().toUpperCase(), Config.HUD_X.get(), Config.HUD_Y.get());
    }

    public static void renderHUD(GuiGraphics guiGraphics, double x, double y, double z, String facing, int xPos, int yPos) {
        Minecraft mc = Minecraft.getInstance();
        int textColor = 0xFFFFFF; // White
        int lineHeight = 10;
        int currentY = yPos;

        if (Config.SHOW_XYZ.get()) {
            int precision = Config.COORD_PRECISION.get();
            String format = "%." + precision + "f";
            String coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            guiGraphics.drawString(mc.font, coordsText, xPos, currentY, textColor);
            currentY += lineHeight;
        }

        if (Config.SHOW_SUBCHUNK.get()) {
            int subX = ((int) Math.floor(x)) & 15;
            int subY = ((int) Math.floor(y)) & 15;
            int subZ = ((int) Math.floor(z)) & 15;
            String subchunkText = String.format("Subchunk: %d %d %d", subX, subY, subZ);
            guiGraphics.drawString(mc.font, subchunkText, xPos, currentY, textColor);
            currentY += lineHeight;
        }

        if (Config.SHOW_FACING.get()) {
            String facingText = "Facing: " + facing;
            guiGraphics.drawString(mc.font, facingText, xPos, currentY, textColor);
        }
    }
}
