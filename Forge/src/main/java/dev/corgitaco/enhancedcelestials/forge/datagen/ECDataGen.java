package dev.corgitaco.enhancedcelestials.forge.datagen;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import dev.corgitaco.enhancedcelestials.forge.datagen.providers.ECItemTagsProvider;
import dev.corgitaco.enhancedcelestials.forge.datagen.providers.ECLunarEventTagsProvider;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnhancedCelestials.MOD_ID)
public class ECDataGen {


    private static RegistrySetBuilder makeBuilder(boolean useMinecraftNameSpace) {

        ResourceKey<Registry<LunarEvent>> lunarEventKey = useMinecraftNameSpace ? ResourceKey.createRegistryKey(new ResourceLocation("lunar/event")) : EnhancedCelestialsRegistry.LUNAR_EVENT_KEY;
        ResourceKey<Registry<LunarDimensionSettings>> dimensionSettingsKey = useMinecraftNameSpace ? ResourceKey.createRegistryKey(new ResourceLocation("lunar/dimension_settings")) : EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY;
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
        CompletableFuture<HolderLookup.Provider> providerForge = event.getLookupProvider().thenApply(provider -> makeBuilder(false).buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));

        final var gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new ECLunarEventTagsProvider(gen.getPackOutput(), false, EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, providerForge, EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), new ECItemTagsProvider(gen.getPackOutput(), providerForge, CompletableFuture.completedFuture(blockTagKey -> Optional.empty()), EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(gen.getPackOutput(), event.getLookupProvider(), makeBuilder(false), Set.of(EnhancedCelestials.MOD_ID, "minecraft")));
    }
}
