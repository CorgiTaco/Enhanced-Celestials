package corgitaco.enhancedcelestials.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import corgitaco.enhancedcelestials.api.entityfilter.EntityFilter;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.mixin.access.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class EnhancedCelestialsRegistry {
    public static final String MOD_ID = "enhancedcelestials";

    public static final ResourceKey<Registry<LunarEvent>> LUNAR_EVENT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/event"));
    public static final ResourceKey<Registry<LunarDimensionSettings>> LUNAR_DIMENSION_SETTINGS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "lunar/dimension_settings"));

    public static final ResourceKey<Registry<Codec<? extends EntityFilter>>> ENTITY_FILTER_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "entity_filter"));

    public static final Registry<Codec<? extends EntityFilter>> ENTITY_FILTER = RegistryAccess.enhancedCelestials_invokeRegisterSimple(ENTITY_FILTER_KEY, Lifecycle.stable(), registry -> EntityFilter.CODEC);

    public static void init(){}
}