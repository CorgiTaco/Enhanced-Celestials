package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.network.packet.LunarContextConstructionPacket;
import corgitaco.enhancedcelestials.network.packet.LunarEventChangedPacket;
import corgitaco.enhancedcelestials.network.packet.LunarForecastChangedPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.List;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Main.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        SIMPLE_CHANNEL.registerMessage(0, LunarContextConstructionPacket.class, LunarContextConstructionPacket::writeToPacket, LunarContextConstructionPacket::readFromPacket, LunarContextConstructionPacket::handle);
        SIMPLE_CHANNEL.registerMessage(1, LunarEventChangedPacket.class, LunarEventChangedPacket::writeToPacket, LunarEventChangedPacket::readFromPacket, LunarEventChangedPacket::handle);
        SIMPLE_CHANNEL.registerMessage(2, LunarForecastChangedPacket.class, LunarForecastChangedPacket::writeToPacket, LunarForecastChangedPacket::readFromPacket, LunarForecastChangedPacket::handle);
    }

    public static void sendToPlayer(ServerPlayer playerEntity, Object objectToSend) {
        SIMPLE_CHANNEL.sendTo(objectToSend, playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAllPlayers(List<ServerPlayer> playerEntities, Object objectToSend) {
        for (ServerPlayer playerEntity : playerEntities) {
            SIMPLE_CHANNEL.sendTo(objectToSend, playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToServer(Object objectToSend) {
        SIMPLE_CHANNEL.sendToServer(objectToSend);
    }
}