package corgitaco.enhancedcelestials.api;

import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class EnhancedCelestialsRegistry {
    public static final String MOD_ID = "enhancedcelestials";

    public static final ResourceKey<Registry<LunarEvent>> LUNAR_EVENT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/event"));
    public static final ResourceKey<Registry<LunarDimensionSettings>> LUNAR_DIMENSION_SETTINGS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/dimension_settings"));

    public static void init() {
    }
}