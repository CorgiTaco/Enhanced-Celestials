package corgitaco.enhancedcelestials.api;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.mixin.access.RegistryAccess;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EnhancedCelestialsRegistry {
    public static final String MOD_ID = "enhancedcelestials";

    // TODO: Make this a registry similar to those of world gen registries.
    public static final Map<ResourceLocation, LunarEvent> DEFAULT_EVENTS = new HashMap<>();

    public static final RegistryKey<Registry<Codec<? extends LunarEvent>>> LUNAR_EVENT_KEY = RegistryKey.getOrCreateRootKey(new ResourceLocation(MOD_ID, "lunar_event"));

    public static final RegistryKey<Registry<Codec<? extends LunarEventClientSettings>>> LUNAR_CLIENT_EVENT_SETTINGS_KEY = RegistryKey.getOrCreateRootKey(new ResourceLocation(MOD_ID, "lunar_event_client"));

    public static final Registry<Codec<? extends LunarEvent>> LUNAR_EVENT = RegistryAccess.invokeCreateRegistry(LUNAR_EVENT_KEY, () -> LunarEvent.CODEC);

    public static final Registry<Codec<? extends LunarEventClientSettings>> LUNAR_CLIENT_EVENT_SETTINGS = RegistryAccess.invokeCreateRegistry(LUNAR_CLIENT_EVENT_SETTINGS_KEY, () -> LunarEventClientSettings.CODEC);
}