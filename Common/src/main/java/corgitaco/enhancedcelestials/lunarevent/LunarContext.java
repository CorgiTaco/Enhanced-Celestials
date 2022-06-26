package corgitaco.enhancedcelestials.lunarevent;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.save.LunarForecastSavedData;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class LunarContext {

    private final LunarForecast lunarForecast;

    private LunarContext(LunarForecast forecast) {
        this.lunarForecast = forecast;
    }

    @Nullable
    public static LunarContext forLevel(Level level, Optional<LunarForecast.SaveData> saveData) {
        Registry<LunarDimensionSettings> lunarDimensionSettingsRegistry = level.registryAccess().registryOrThrow(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY);
        Registry<LunarEvent> lunarEventRegistry = level.registryAccess().registryOrThrow(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY);
        ResourceLocation location = level.dimension().location();
        Optional<Holder<LunarDimensionSettings>> possibleLunarDimensionSettings = lunarDimensionSettingsRegistry.getHolder(ResourceKey.create(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, location));

        if (possibleLunarDimensionSettings.isPresent()) {
            Holder<LunarDimensionSettings> lunarDimensionSettings = possibleLunarDimensionSettings.get();
            LunarForecast.SaveData forecastSaveData = saveData.orElseGet(() -> LunarForecastSavedData.get(level).getForecastSaveData());
            LunarForecast forecast;
            long dayTime = level.getDayTime();
            if (forecastSaveData == null) {
                forecast = new LunarForecast(lunarDimensionSettings, lunarEventRegistry, dayTime);
            } else {
                forecast = new LunarForecast(lunarDimensionSettings, lunarEventRegistry, dayTime, forecastSaveData);
            }
            return new LunarContext(forecast);
        }
        return null;
    }

    public void tick(Level world) {
        this.lunarForecast.tick(world);
        if (world.getGameTime() % 2400 == 0) {
            save(world);
        }
    }

    public void save(Level world) {
        LunarForecastSavedData.get(world).setForecastSaveData(this.lunarForecast.saveData());
    }

    public LunarForecast getLunarForecast() {
        return lunarForecast;
    }

}
