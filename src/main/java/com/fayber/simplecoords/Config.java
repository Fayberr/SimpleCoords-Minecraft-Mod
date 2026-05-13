package com.fayber.simplecoords;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Overall toggle for the HUD")
            .translation("simplecoords.config.enabled")
            .define("enabled", true);

    public static final ModConfigSpec.BooleanValue USE_CAMERA = BUILDER
            .comment("Use camera coordinates instead of player coordinates (useful for Freecam)")
            .translation("simplecoords.config.use_camera")
            .define("use_camera", true);

    public static final ModConfigSpec.BooleanValue SHOW_XYZ = BUILDER
            .comment("Show the XYZ coordinates")
            .translation("simplecoords.config.show_xyz")
            .define("show_xyz", true);

    public static final ModConfigSpec.BooleanValue SHOW_SUBCHUNK = BUILDER
            .comment("Show the subchunk coordinates")
            .translation("simplecoords.config.show_subchunk")
            .define("show_subchunk", true);

    public static final ModConfigSpec.BooleanValue SHOW_FACING = BUILDER
            .comment("Show the facing direction")
            .translation("simplecoords.config.show_facing")
            .define("show_facing", true);

    public static final ModConfigSpec.BooleanValue SHOW_INTERCARDINAL = BUILDER
            .comment("Show intercardinal directions (NE, SW, etc.)")
            .translation("simplecoords.config.show_intercardinal")
            .define("show_intercardinal", false);

    public static final ModConfigSpec.BooleanValue SHOW_BACKGROUND = BUILDER
            .comment("Show a semi-transparent background behind the HUD")
            .translation("simplecoords.config.show_background")
            .define("show_background", true);

    public static final ModConfigSpec.IntValue COORD_PRECISION = BUILDER
            .comment("Number of decimal places for coordinates (0-5)")
            .translation("simplecoords.config.precision")
            .defineInRange("precision", 0, 0, 5);

    public static final ModConfigSpec.IntValue HUD_X = BUILDER
            .comment("X position of the HUD")
            .translation("simplecoords.config.x")
            .defineInRange("hud_x", 5, 0, 4000);

    public static final ModConfigSpec.IntValue HUD_Y = BUILDER
            .comment("Y position of the HUD")
            .translation("simplecoords.config.y")
            .defineInRange("hud_y", 5, 0, 4000);

    static final ModConfigSpec SPEC = BUILDER.build();
}
