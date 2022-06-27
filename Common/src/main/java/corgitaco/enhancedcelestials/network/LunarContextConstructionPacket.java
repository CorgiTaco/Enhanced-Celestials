package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.lunarevent.LunarContext;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class LunarContextConstructionPacket implements S2CPacket {

    private final LunarForecast.Data data;

    public LunarContextConstructionPacket(LunarForecast.Data data) {
        this.data = data;
    }

    public static LunarContextConstructionPacket readFromPacket(FriendlyByteBuf buf) {
        try {
            return new LunarContextConstructionPacket(buf.readWithCodec(LunarForecast.Data.CODEC));
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        try {
            buf.writeWithCodec(LunarForecast.Data.CODEC, this.data);
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Context packet could not be written to. This is really really bad...\n\n" + e.getMessage());

        }
    }

    @Override
    public void handle(Level level) {
        if (level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (lunarContext == null) {
                ((EnhancedCelestialsWorldData) level).setLunarContext(LunarContext.forLevel(level, Optional.of(this.data)));
            } else {
                EnhancedCelestials.LOGGER.warn("Attempted lunar context reconstruction from:");
                new Throwable().printStackTrace();
            }
        }
    }
}