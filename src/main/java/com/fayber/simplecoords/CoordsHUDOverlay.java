package com.fayber.simplecoords;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class CoordsHUDOverlay {
    private static long lastLogTime = 0;

    public static void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!Config.data.enabled || client.player == null || client.options.hudHidden) {
            return;
        }

        Entity target = Config.data.use_camera && client.getCameraEntity() != null ? client.getCameraEntity() : client.player;
        float partialTick = tickCounter.getTickProgress(true);

        double x = MathHelper.lerp((double)partialTick, target.lastRenderX, target.getX());
        double y = MathHelper.lerp((double)partialTick, target.lastRenderY, target.getY());
        double z = MathHelper.lerp((double)partialTick, target.lastRenderZ, target.getZ());

        float yaw = target.getYaw(partialTick);
        String facing = getFacingDirection(yaw, Config.data.show_intercardinal);

        renderHUD(drawContext, x, y, z, facing, Config.data.hud_x, Config.data.hud_y);
    }

    private static String getFacingDirection(float yaw, boolean includeIntercardinal) {
        float degree = MathHelper.wrapDegrees(yaw);
        
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

    public static void renderHUD(DrawContext drawContext, double x, double y, double z, String facing, int xPos, int yPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        int textColor = 0xFFFFFFFF; // White
        int lineHeight = 10;

        List<String> lines = new ArrayList<>();

        if (Config.data.show_xyz) {
            int precision = Config.data.precision;
            String coordsText;
            if (precision == 0) {
                coordsText = String.format("XYZ: %d / %d / %d", MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            } else {
                String format = "%." + precision + "f";
                coordsText = String.format("XYZ: " + format + " / " + format + " / " + format, x, y, z);
            }
            lines.add(coordsText);
        }

        if (Config.data.show_subchunk) {
            int subX = MathHelper.floor(x) & 15;
            int subY = MathHelper.floor(y) & 15;
            int subZ = MathHelper.floor(z) & 15;
            lines.add(String.format("Subchunk: %d %d %d", subX, subY, subZ));
        }

        if (Config.data.show_facing) {
            lines.add("Facing: " + facing);
        }

        if (lines.isEmpty()) return;

        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, client.textRenderer.getWidth(line));
        }

        if (Config.data.show_background) {
            drawContext.fill(xPos - 2, yPos - 2, xPos + maxWidth + 2, yPos + (lines.size() * lineHeight), 0x80000000);
        }

        int currentY = yPos;
        for (String line : lines) {
            drawContext.drawTextWithShadow(client.textRenderer, line, xPos, currentY, textColor);
            currentY += lineHeight;
        }
    }
}
