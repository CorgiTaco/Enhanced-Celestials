package dev.corgitaco.enhancedcelestials.network;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import dev.corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record LunarContextConstructionPacket(LunarForecast.Data data) implements ECPacket {


    public static final StreamCodec<RegistryFriendlyByteBuf, LunarContextConstructionPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(LunarForecast.Data.CODEC), LunarContextConstructionPacket::data,
            LunarContextConstructionPacket::new
    );

    public static final Type<LunarContextConstructionPacket> TYPE = new Type<>(EnhancedCelestials.createLocation("lunar_context"));


    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
        if (level != null) {
            EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (enhancedCelestialsContext == null) {
                ((EnhancedCelestialsWorldData) level).setLunarContext(EnhancedCelestialsContext.forLevel(level, Optional.of(this.data)));
            } else {
                EnhancedCelestials.LOGGER.warn("Attempted lunar context reconstruction from:");
                new Throwable().printStackTrace();
            }
        }
    }

    @Override
    public Type<? extends LunarContextConstructionPacket> type() {
        return TYPE;
    }
}