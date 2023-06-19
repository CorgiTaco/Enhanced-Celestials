package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnhancedCelestials.MOD_ID)
public class ECModBusEventsHandler {

    @SubscribeEvent
    public static void registerDatapack(DataPackRegistryEvent.NewRegistry event) {
        // Disgusting workaround bc Forge prefixes name spaces. We want to datagen fabric so this is the gross alternative.
        if (Boolean.parseBoolean(System.getProperty("datagen", "false"))) {
            event.dataPackRegistry(ResourceKey.createRegistryKey(new ResourceLocation(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY.location().getPath())), LunarEvent.DIRECT_CODEC, LunarEvent.DIRECT_CODEC);
            event.dataPackRegistry(ResourceKey.createRegistryKey(new ResourceLocation(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY.location().getPath())), LunarDimensionSettings.CODEC, LunarDimensionSettings.CODEC);
        }
        event.dataPackRegistry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, LunarEvent.DIRECT_CODEC, LunarEvent.DIRECT_CODEC);
        event.dataPackRegistry(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, LunarDimensionSettings.CODEC, LunarDimensionSettings.CODEC);

    }
}