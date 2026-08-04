package dev.corgitaco.enhancedcelestials.fabric.client;

import dev.corgitaco.enhancedcelestials.client.EC2AnnouncementTrigger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class EnhancedCelestialsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> EC2AnnouncementTrigger.onLogin());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> EC2AnnouncementTrigger.onDisconnect());
        ClientTickEvents.END_CLIENT_TICK.register(client -> EC2AnnouncementTrigger.onClientTick());
    }
}
