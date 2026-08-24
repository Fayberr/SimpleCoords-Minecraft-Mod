package com.fayber.simplecoords;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ClientCommandHandler {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            dispatcher.register(
                ClientCommands.literal("simplecoords")
                    .executes(context -> {
                        // Client commands run on the game thread in 26.1.2; no execute() wrapper needed.
                        Minecraft.getInstance().setScreen(new CustomConfigScreen(null));
                        return 1;
                    })
                    .then(ClientCommands.literal("config")
                        .executes(context -> {
                            Minecraft.getInstance().setScreen(new CustomConfigScreen(null));
                            return 1;
                        })
                    )
                    .then(ClientCommands.literal("toggle")
                        .executes(context -> {
                            Config.data.enabled = !Config.data.enabled;
                            Config.save();
                            context.getSource().sendFeedback(Component.literal("SimpleCoords HUD is now " + (Config.data.enabled ? "Enabled" : "Disabled")));
                            return 1;
                        })
                    )
                    .then(ClientCommands.literal("set")
                        .then(ClientCommands.literal("use_camera")
                            .then(ClientCommands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.use_camera = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Use Camera Position: " + Config.data.use_camera));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommands.literal("show_xyz")
                            .then(ClientCommands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_xyz = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show XYZ: " + Config.data.show_xyz));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommands.literal("show_subchunk")
                            .then(ClientCommands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_subchunk = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show Subchunk: " + Config.data.show_subchunk));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommands.literal("show_facing")
                            .then(ClientCommands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    Config.data.show_facing = BoolArgumentType.getBool(context, "value");
                                    Config.save();
                                    context.getSource().sendFeedback(Component.literal("Show Facing: " + Config.data.show_facing));
                                    return 1;
                                })
                            )
                        )
                        .then(ClientCommands.literal("precision")
                            .then(ClientCommands.argument("value", IntegerArgumentType.integer(0, 5))
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
