package com.fayber.simplecoords;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public class CoordsHUDOverlay {

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Config.ENABLED.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        double x, y, z;
        String facing;

        if (Config.USE_CAMERA.get()) {
            Camera camera = mc.gameRenderer.getMainCamera();
            Vec3 pos = camera.getPosition();
            x = pos.x;
            y = pos.y;
            z = pos.z;
            facing = Direction.orderedByNearest(camera.getEntity()).length > 0 ? Direction.fromYRot(camera.getYRot()).getName().toUpperCase() : "UNKNOWN";
        } else {
            x = mc.player.getX();
            y = mc.player.getY();
            z = mc.player.getZ();
            facing = mc.player.getDirection().getName().toUpperCase();
        }

        renderHUD(guiGraphics, x, y, z, facing, Config.HUD_X.get(), Config.HUD_Y.get());
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
