package dev.corgitaco.enhancedcelestials.api;

import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEventDimensionChance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class EnhancedCelestialsRegistry {
    public static final String MOD_ID = "enhancedcelestials";

    public static final ResourceKey<Registry<LunarEvent>> LUNAR_EVENT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/event"));
    public static final ResourceKey<Registry<LunarEventDimensionChance>> LUNAR_EVENT_DIMENSION_CHANCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/event/dimension_chance"));
    public static final ResourceKey<Registry<LunarDimensionSettings>> LUNAR_DIMENSION_SETTINGS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/dimension_settings"));

    public static void init() {
    }
}