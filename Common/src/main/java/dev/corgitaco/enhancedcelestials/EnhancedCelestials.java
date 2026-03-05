package dev.corgitaco.enhancedcelestials;

import com.mojang.logging.LogUtils;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.Optional;

public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final TrackedDataKey<EnhancedCelestialsLunarForecastWorldData> LUNAR_FORECAST_WORLD_DATA = TrackedDataRegistries.LEVEL.register(
            createLocation("lunar_forecast"),
            EnhancedCelestialsLunarForecastWorldData.class,
            EnhancedCelestialsLunarForecastWorldData::factory
    );

    public static Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData(Level level) {
        return TrackedDataRegistries.LEVEL.get(LUNAR_FORECAST_WORLD_DATA, level);
    }

    public EnhancedCelestials() {
    }

    public static void commonSetup() {
    }


    public static ResourceLocation createLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
