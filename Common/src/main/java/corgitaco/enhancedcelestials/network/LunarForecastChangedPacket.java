package corgitaco.enhancedcelestials.network;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class LunarForecastChangedPacket implements S2CPacket {

    private final LunarForecast lunarForecast;

    public LunarForecastChangedPacket(LunarForecast lunarForecast) {
        this.lunarForecast = lunarForecast;
    }


    public static LunarForecastChangedPacket readFromPacket(FriendlyByteBuf buf) {
        try {
            return new LunarForecastChangedPacket(buf.readWithCodec(LunarForecast.CODEC));
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Forecast packet could not be read. This is really really bad...\n\n" + e.getMessage());
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        try {
            buf.writeWithCodec(LunarForecast.CODEC, this.lunarForecast);
        } catch (Exception e) {
            throw new IllegalStateException("Lunar Forecast packet could not be written to. This is really really bad...\n\n" + e.getMessage());
        }
    }

    @Override
    public void handle(Level level) {
        if (level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (lunarContext != null) {
                lunarContext.getLunarForecast().getForecast().clear();
                lunarContext.getLunarForecast().getForecast().addAll(this.lunarForecast.getForecast());
                lunarContext.getLunarForecast().setLastCheckedGameTime(this.lunarForecast.getLastCheckedGameTime());
            }
        }
    }
}