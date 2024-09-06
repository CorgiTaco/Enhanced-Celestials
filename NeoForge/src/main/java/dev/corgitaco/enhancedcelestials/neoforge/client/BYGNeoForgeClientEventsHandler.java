package dev.corgitaco.enhancedcelestials.neoforge.client;

import dev.corgitaco.enhancedcelestials.client.ECEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BYGNeoForgeClientEventsHandler {

    @SubscribeEvent
    public static void ec_onEntityRenderersEvent$RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ECEntityRenderers.register(event::registerEntityRenderer);
    }
}