package dev.corgitaco.enhancedcelestials.network;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface ECPacket extends CustomPacketPayload {
    List<Handler<?>> PACKETS = Util.make(new ArrayList<>(), list -> {
        EnhancedCelestials.LOGGER.info("Initializing network...");
        list.add(new Handler<>(LunarContextConstructionPacket.TYPE, PacketDirection.SERVER_TO_CLIENT, LunarContextConstructionPacket.CODEC, LunarContextConstructionPacket::handle));
        list.add(new Handler<>(LunarForecastChangedPacket.TYPE, PacketDirection.CLIENT_TO_SERVER, LunarForecastChangedPacket.CODEC, LunarForecastChangedPacket::handle));
        EnhancedCelestials.LOGGER.info("Initialized network!");
    });


    void handle(@Nullable Level level, @Nullable Player player);


    record Handler<T extends ECPacket>(Type<T> type, PacketDirection direction,
                                       StreamCodec<RegistryFriendlyByteBuf, T> serializer, Handle<T> handle) {
    }

    enum PacketDirection {
        SERVER_TO_CLIENT,
        CLIENT_TO_SERVER,
        BI_DIRECTIONAL
    }

    @FunctionalInterface
    interface Handle<T extends ECPacket> {
        void handle(T packet, Level level, Player player);
    }
}
