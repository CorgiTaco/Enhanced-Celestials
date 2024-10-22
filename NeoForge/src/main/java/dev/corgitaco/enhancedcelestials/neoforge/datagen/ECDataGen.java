package dev.corgitaco.enhancedcelestials.neoforge.datagen;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import dev.corgitaco.enhancedcelestials.neoforge.datagen.providers.ECItemTagsProvider;
import dev.corgitaco.enhancedcelestials.neoforge.datagen.providers.ECLunarEventTagsProvider;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructures;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = EnhancedCelestials.MOD_ID)
public class ECDataGen {


    private static RegistrySetBuilder makeBuilder(boolean useMinecraftNameSpace) {

        ResourceKey<Registry<LunarEvent>> lunarEventKey = useMinecraftNameSpace ? ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("lunar/event")) : EnhancedCelestialsRegistry.LUNAR_EVENT_KEY;
        ResourceKey<Registry<LunarDimensionSettings>> dimensionSettingsKey = useMinecraftNameSpace ? ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("lunar/dimension_settings")) : EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY;
        return new RegistrySetBuilder().add(lunarEventKey, pContext -> {
            DefaultLunarEvents.LUNAR_EVENT_FACTORIES.forEach((lunarEventResourceKey, factory) -> {
                pContext.register(ResourceKey.create(lunarEventKey, lunarEventResourceKey.location()), factory.generate(pContext));
            });
        }).add(dimensionSettingsKey, pContext -> {
            DefaultLunarDimensionSettings.LUNAR_DIMENSION_SETTINGS_FACTORIES.forEach((lunarEventResourceKey, factory) -> {
                pContext.register(ResourceKey.create(dimensionSettingsKey, lunarEventResourceKey.location()), factory.generate(pContext));
            });
        }).add(Registries.STRUCTURE, pContext -> {
            if (EnhancedCelestials.NEW_CONTENT) {
                ECStructures.STRUCTURE_FACTORIES.forEach((resourceKey, factory) -> {
                    pContext.register(resourceKey, factory.generate(pContext));
                });
            }
        });
    }


    @SubscribeEvent
    static void onDatagen(final GatherDataEvent event) {
        EnhancedCelestials.commonSetup();
        Cloner.Factory factory = new Cloner.Factory();
        RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(registryData -> registryData.runWithArguments(factory::addCodec));

        final var gen = event.getGenerator();
        DatapackBuiltinEntriesProvider datapackBuiltinEntriesProvider = new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), makeBuilder(false), Set.of("minecraft", EnhancedCelestials.MOD_ID));
        gen.addProvider(event.includeServer(), datapackBuiltinEntriesProvider);


        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackBuiltinEntriesProvider.getRegistryProvider();

        gen.addProvider(event.includeServer(), new ECLunarEventTagsProvider(gen.getPackOutput(), false, EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, lookupProvider, EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), new ECItemTagsProvider(gen.getPackOutput(), lookupProvider, CompletableFuture.completedFuture(blockTagKey -> Optional.empty()), EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));
    }
}
