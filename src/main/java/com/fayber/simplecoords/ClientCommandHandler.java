package com.fayber.simplecoords;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class ClientCommandHandler {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("simplecoords")
                    .then(ClientCommandManager.literal("toggle")
                        .executes(context -> {
                            Config.data.enabled = !Config.data.enabled;
                            Config.save();
                            
                            String status = Config.data.enabled ? "Enabled" : "Disabled";
                            context.getSource().sendFeedback(Text.literal("SimpleCoords HUD is now " + status));
                            return 1;
                        })
                    )
            );
        });
    }
}
