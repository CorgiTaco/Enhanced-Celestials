package dev.corgitaco.enhancedcelestials.neoforge;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = EnhancedCelestials.MOD_ID)
public class ECModBusEventsHandler {

    @SubscribeEvent
    public static void registerDatapack(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, LunarEvent.DIRECT_CODEC, LunarEvent.DIRECT_CODEC);
        event.dataPackRegistry(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, LunarDimensionSettings.CODEC, LunarDimensionSettings.CODEC);
    }
}