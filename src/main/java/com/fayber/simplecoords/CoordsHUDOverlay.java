package com.fayber.simplecoords;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CoordsHUDOverlay {

    public static void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!Config.data.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        Entity target = Config.data.use_camera && client.getCameraEntity() != null ? client.getCameraEntity() : client.player;
        float partialTick = tickCounter.getTickProgress(true);

        double x = MathHelper.lerp((double)partialTick, target.lastRenderX, target.getX());
        double y = MathHelper.lerp((double)partialTick, target.lastRenderY, target.getY());
        double z = MathHelper.lerp((double)partialTick, target.lastRenderZ, target.getZ());
        
        String facing = target.getHorizontalFacing().asString().toUpperCase();

        renderHUD(drawContext, x, y, z, facing, Config.data.hud_x, Config.data.hud_y);
    }

    public static void renderHUD(DrawContext drawContext, double x, double y, double z, String facing, int xPos, int yPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        int textColor = 0xFFFFFF; // White
        int lineHeight = 10;
        int currentY = yPos;

        if (Config.data.show_xyz) {
            int precision = Config.data.precision;
            String coordsText;
            
            if (precision == 0) {
                coordsText = String.format("XYZ: %d / %d / %d", MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            } else {
                String format = "%." + precision + "f";
                coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            }
            
            drawContext.drawText(client.textRenderer, coordsText, xPos, currentY, textColor, false);
            currentY += lineHeight;
        }

        if (Config.data.show_subchunk) {
            int subX = MathHelper.floor(x) & 15;
            int subY = MathHelper.floor(y) & 15;
            int subZ = MathHelper.floor(z) & 15;
            String subchunkText = String.format("Subchunk: %d %d %d", subX, subY, subZ);
            drawContext.drawText(client.textRenderer, subchunkText, xPos, currentY, textColor, false);
            currentY += lineHeight;
        }

        if (Config.data.show_facing) {
            String facingText = "Facing: " + facing;
            drawContext.drawText(client.textRenderer, facingText, xPos, currentY, textColor, false);
        }
    }
}
