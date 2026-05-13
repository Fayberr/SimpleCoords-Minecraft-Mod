package com.fayber.simplecoords;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ClientCommandHandler {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("simplecoords")
                    .executes(context -> {
                        MinecraftClient.getInstance().execute(() -> {
                            MinecraftClient.getInstance().setScreen(new CustomConfigScreen(null));
                        });
                        return 1;
                    })
                    .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MinecraftClient.getInstance().execute(() -> {
                                MinecraftClient.getInstance().setScreen(new CustomConfigScreen(null));
                            });
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("toggle")
                        .executes(context -> {
                            Config.data.enabled = !Config.data.enabled;
                            Config.save();
                            context.getSource().sendFeedback(Text.literal("SimpleCoords HUD is now " + (Config.data.enabled ? "Enabled" : "Disabled")));
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.literal("use_camera")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.use_camera = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Text.literal("Use Camera Position: " + Config.data.use_camera));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_xyz")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_xyz = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Text.literal("Show XYZ: " + Config.data.show_xyz));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_subchunk")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_subchunk = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Text.literal("Show Subchunk: " + Config.data.show_subchunk));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("show_facing")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_facing = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Text.literal("Show Facing: " + Config.data.show_facing));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommandManager.literal("precision")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 5))
                                .executes(context -> {
                                    Config.data.precision = IntegerArgumentType.getInteger(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Text.literal("Decimal Precision: " + Config.data.precision));
                                    return 1;
                                })
                            )
                        )
                    )
            );
        });
    }
}
