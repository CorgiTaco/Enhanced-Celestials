package dev.corgitaco.enhancedcelestials.neoforge.network;

import corgitaco.corgilib.network.Packet;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.network.ECPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EnhancedCelestials.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeNetworkHandler {


    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        ECPacket.PACKETS.forEach(handler -> register(handler, registrar));
    }

    private static <T extends ECPacket> void register(ECPacket.Handler<T> handler, PayloadRegistrar registrar) {
        if (handler.direction() == ECPacket.PacketDirection.SERVER_TO_CLIENT) {
            registrar.playToClient(handler.type(), handler.serializer(), (arg, iPayloadContext) -> arg.handle(iPayloadContext.player().level(), iPayloadContext.player()));
        }

        if (handler.direction() == ECPacket.PacketDirection.CLIENT_TO_SERVER) {
            registrar.playToServer(handler.type(), handler.serializer(), (arg, iPayloadContext) -> arg.handle(iPayloadContext.player().level(), iPayloadContext.player()));
        }

        if (handler.direction() == ECPacket.PacketDirection.BI_DIRECTIONAL) {
            registrar.playBidirectional(handler.type(), handler.serializer(), (arg, iPayloadContext) -> arg.handle(iPayloadContext.player().level(), iPayloadContext.player()));
        }
    }
}