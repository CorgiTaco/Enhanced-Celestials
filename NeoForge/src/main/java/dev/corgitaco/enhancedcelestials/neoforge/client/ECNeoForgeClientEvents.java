package dev.corgitaco.enhancedcelestials.neoforge.client;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.client.EC2AnnouncementTrigger;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = EnhancedCelestials.MOD_ID, value = Dist.CLIENT)
public class ECNeoForgeClientEvents {

    @SubscribeEvent
    public static void onLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        EC2AnnouncementTrigger.onLogin();
    }

    @SubscribeEvent
    public static void onLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        EC2AnnouncementTrigger.onDisconnect();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        EC2AnnouncementTrigger.onClientTick();
    }
}
