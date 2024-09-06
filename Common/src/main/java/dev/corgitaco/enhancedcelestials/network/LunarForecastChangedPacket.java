package dev.corgitaco.enhancedcelestials.network;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import dev.corgitaco.enhancedcelestials.lunarevent.LunarEventInstance;
import dev.corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record LunarForecastChangedPacket(LunarForecast.Data lunarForecast, boolean isNight) implements ECPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, LunarForecastChangedPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(LunarForecast.Data.CODEC), LunarForecastChangedPacket::lunarForecast,
            ByteBufCodecs.BOOL, LunarForecastChangedPacket::isNight,
            LunarForecastChangedPacket::new
    );

    public static final Type<LunarForecastChangedPacket> TYPE = new Type<>(EnhancedCelestials.createLocation("lunar_forecast_changed"));



    public LunarForecastChangedPacket(LunarForecast forecast, boolean isNight) {
        this(forecast.data(), isNight);
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
        if (level != null) {
            EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
            if (enhancedCelestialsContext != null) {
                LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();
                lunarForecast.getForecast().clear();
                lunarForecast.getForecast().addAll(this.lunarForecast.forecast());

                lunarForecast.getPastEvents().clear();
                lunarForecast.getPastEvents().addAll(this.lunarForecast.pastEvents());

                lunarForecast.setLastCheckedGameTime(this.lunarForecast.lastCheckedGameTime());

                if (!lunarForecast.getForecast().isEmpty()) {
                    LunarEventInstance lunarEventInstance = lunarForecast.getForecast().get(0);
                    LunarDimensionSettings lunarDimensionSettings = lunarForecast.getDimensionSettingsHolder().value();
                    long currentDay = level.getDayTime() / lunarDimensionSettings.dayLength();
                    if (lunarEventInstance.active(currentDay) && this.isNight) {
                        lunarForecast.setCurrentEvent(lunarEventInstance.getLunarEventKey());
                    } else {
                        lunarForecast.setCurrentEvent(lunarDimensionSettings.defaultEvent());
                    }
                }
            }
        }
    }

    @Override
    public Type<? extends LunarForecastChangedPacket> type() {
        return TYPE;
    }
}