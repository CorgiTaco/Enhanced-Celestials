package corgitaco.enchancedcelestials.data.network;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EnhancedCelestials.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        SIMPLE_CHANNEL.registerMessage(0, LunarEventPacket.class, LunarEventPacket::writeToPacket, LunarEventPacket::readFromPacket, LunarEventPacket::handle);
    }

    public static void sendToClient(ServerPlayerEntity playerEntity, Object objectToSend) {
        SIMPLE_CHANNEL.sendTo(objectToSend, playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object objectToSend) {
        SIMPLE_CHANNEL.sendToServer(objectToSend);
    }
}