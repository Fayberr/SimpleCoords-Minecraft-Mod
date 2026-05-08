package com.fayber.simplecoords;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber(modid = SimpleCoords.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientCommandHandler {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("simplecoords")
                .then(Commands.literal("toggle")
                    .executes(context -> {
                        boolean newValue = !Config.ENABLED.get();
                        Config.ENABLED.set(newValue);
                        Config.ENABLED.save();
                        
                        String status = newValue ? "Enabled" : "Disabled";
                        context.getSource().sendSuccess(() -> Component.literal("SimpleCoords HUD is now " + status), false);
                        return 1;
                    })
                )
        );
    }
}
