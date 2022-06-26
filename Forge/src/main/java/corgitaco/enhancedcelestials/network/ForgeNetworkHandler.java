package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgeNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(EnhancedCelestials.MOD_ID, "network"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void init() {
        EnhancedCelestials.LOGGER.debug(String.format("Initializing %s network...", EnhancedCelestials.MOD_ID));
        SIMPLE_CHANNEL.registerMessage(0, LunarContextConstructionPacket.class, LunarContextConstructionPacket::write, LunarContextConstructionPacket::readFromPacket, ForgeNetworkHandler::handle);
        SIMPLE_CHANNEL.registerMessage(1, LunarForecastChangedPacket.class, LunarForecastChangedPacket::write, LunarForecastChangedPacket::readFromPacket, ForgeNetworkHandler::handle);
        EnhancedCelestials.LOGGER.info(String.format("Initialized %s network!", EnhancedCelestials.MOD_ID));
    }

    public static <T extends S2CPacket> void sendToPlayer(ServerPlayer playerEntity, T packet) {
        SIMPLE_CHANNEL.sendTo(packet, playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object objectToSend) {
        SIMPLE_CHANNEL.sendToServer(objectToSend);
    }

    public static <T extends S2CPacket> void handle(T packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                Client.clientHandle(packet);
            });
            context.setPacketHandled(true);
        }
    }


    private static class Client {
        private static <T extends S2CPacket> void clientHandle(T packet) {
            packet.handle(Minecraft.getInstance().level);
        }
    }
}