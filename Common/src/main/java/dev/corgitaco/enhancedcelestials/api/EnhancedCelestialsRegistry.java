package dev.corgitaco.enhancedcelestials.api;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class EnhancedCelestialsRegistry {
    public static final String MOD_ID = "enhancedcelestials";

    public static final ResourceKey<Registry<LunarEvent>> LUNAR_EVENT_KEY = ResourceKey.createRegistryKey(EnhancedCelestials.createLocation("lunar/event"));
    public static final ResourceKey<Registry<LunarDimensionSettings>> LUNAR_DIMENSION_SETTINGS_KEY = ResourceKey.createRegistryKey(EnhancedCelestials.createLocation("lunar/dimension_settings"));

    public static void init() {
    }
}