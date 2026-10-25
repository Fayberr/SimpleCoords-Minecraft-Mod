package com.fayber.simplecoords;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ClientCommandHandler {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            dispatcher.register(
                ClientCommandManager.literal("simplecoords")
                    .executes(context -> {
                        Minecraft.getInstance().setScreen(ConfigScreenRouter.create(null));
                        return 1;
                    })
                    .then(ClientCommandManager.literal("hud")
                        .executes(context -> {
                            Minecraft.getInstance().setScreen(new HUDEditorScreen(null));
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            Minecraft.getInstance().setScreen(ConfigScreenRouter.create(null));
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("toggle")
                        .executes(context -> {
                            Config.data.enabled = !Config.data.enabled;
                            Config.save();
                            context.getSource().sendFeedback(Component.literal("SimpleCoords HUD is now " + (Config.data.enabled ? "Enabled" : "Disabled")));
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.literal("use_camera")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.use_camera = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Use Camera Position: " + Config.data.use_camera));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_xyz")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_xyz = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show XYZ: " + Config.data.show_xyz));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_subchunk")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_subchunk = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show Subchunk: " + Config.data.show_subchunk));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_facing")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_facing = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show Facing: " + Config.data.show_facing));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_biome")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_biome = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show Biome: " + Config.data.show_biome));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_target")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_target = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("show_target: " + Config.data.show_target));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_target_subchunk")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_target_subchunk = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("show_target_subchunk: " + Config.data.show_target_subchunk));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_target_type")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_target_type = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("show_target_type: " + Config.data.show_target_type));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_target_state")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_target_state = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("show_target_state: " + Config.data.show_target_state));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("precision")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 5))
                                .executes(context -> {
                                    Config.data.precision = IntegerArgumentType.getInteger(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Decimal Precision: " + Config.data.precision));
                                    return 1;
                                })
                            )
                        )
                    )
            );
        });
    }
}
