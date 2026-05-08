package com.fayber.simplecoords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

public class CoordsHUDOverlay {

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Config.ENABLED.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        // To match F3 exactly:
        // 1. We use the 'Camera Entity' (this is the player in F5, and the ghost in Freecam)
        // 2. We interpolate the position using partial ticks for smooth, matching numbers
        Entity target = mc.getCameraEntity() != null ? mc.getCameraEntity() : mc.player;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);

        double x = Mth.lerp((double)partialTick, target.xo, target.getX());
        double y = Mth.lerp((double)partialTick, target.yo, target.getY());
        double z = Mth.lerp((double)partialTick, target.zo, target.getZ());
        
        String facing = target.getDirection().getName().toUpperCase();

        renderHUD(guiGraphics, x, y, z, facing, Config.HUD_X.get(), Config.HUD_Y.get());
    }

    public static void renderHUD(GuiGraphics guiGraphics, double x, double y, double z, String facing, int xPos, int yPos) {
        Minecraft mc = Minecraft.getInstance();
        int textColor = 0xFFFFFF; // White
        int lineHeight = 10;
        int currentY = yPos;

        if (Config.SHOW_XYZ.get()) {
            int precision = Config.COORD_PRECISION.get();
            String coordsText;
            
            if (precision == 0) {
                // Precision 0 = Match the "Block:" line in F3 (Integer floor)
                coordsText = String.format("XYZ: %d / %d / %d", Mth.floor(x), Mth.floor(y), Mth.floor(z));
            } else {
                // Decimals = Match the "XYZ:" line in F3
                String format = "%." + precision + "f";
                coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            }
            
            guiGraphics.drawString(mc.font, coordsText, xPos, currentY, textColor);
            currentY += lineHeight;
        }

        if (Config.SHOW_SUBCHUNK.get()) {
            // Subchunk logic: match the F3 line using floored integers
            int subX = Mth.floor(x) & 15;
            int subY = Mth.floor(y) & 15;
            int subZ = Mth.floor(z) & 15;
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
