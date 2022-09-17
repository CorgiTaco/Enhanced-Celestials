package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import corgitaco.enhancedcelestials.meteor.MeteorContext;
import corgitaco.enhancedcelestials.save.LunarForecastSavedData;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnhancedCelestialsContext {

    private final LunarForecast lunarForecast;
    private final MeteorContext meteorContext;

    private EnhancedCelestialsContext(LunarForecast forecast) {
        this.lunarForecast = forecast;
        this.meteorContext = new MeteorContext();
    }

    @Nullable
    public static EnhancedCelestialsContext forLevel(Level level, Optional<LunarForecast.Data> saveData) {
        Registry<LunarDimensionSettings> lunarDimensionSettingsRegistry = level.registryAccess().registryOrThrow(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY);
        Registry<LunarEvent> lunarEventRegistry = level.registryAccess().registryOrThrow(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY);
        ResourceLocation location = level.dimension().location();
        Optional<Holder<LunarDimensionSettings>> possibleLunarDimensionSettings = lunarDimensionSettingsRegistry.getHolder(ResourceKey.create(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, location));

        if (possibleLunarDimensionSettings.isPresent()) {
            Holder<LunarDimensionSettings> lunarDimensionSettings = possibleLunarDimensionSettings.get();
            LunarForecast.Data forecastData = saveData.orElseGet(() -> LunarForecastSavedData.get(level).getForecastSaveData());
            LunarForecast forecast;
            long dayTime = level.getDayTime();
            if (forecastData == null) {
                forecast = new LunarForecast(lunarDimensionSettings, lunarEventRegistry, dayTime);
            } else {
                forecast = new LunarForecast(lunarDimensionSettings, lunarEventRegistry, dayTime, forecastData);
            }
            return new EnhancedCelestialsContext(forecast);
        }
        return null;
    }

    public void tick(Level world) {
        this.lunarForecast.tick(world);
        if (world.getGameTime() % 2400 == 0) {
            save(world);
        }
    }

    public void chunkTick(Level level, ChunkAccess chunkAccess) {
        meteorContext.chunkTick(level, chunkAccess);
    }

    public void save(Level world) {
        LunarForecastSavedData.get(world).setForecastSaveData(this.lunarForecast.data());
    }

    public LunarForecast getLunarForecast() {
        return lunarForecast;
    }

}
