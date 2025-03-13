package dev.corgitaco.enhancedcelestials.forge.datagen;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.datagen.ECDatagen;
import dev.corgitaco.enhancedcelestials.datagen.providers.ECItemTagsProvider;
import dev.corgitaco.enhancedcelestials.datagen.providers.ECLunarEventTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnhancedCelestials.MOD_ID)
public class ECForgeDataGen {

    @SubscribeEvent
    static void onDatagen(final GatherDataEvent event) {
        EnhancedCelestials.commonSetup();
        CompletableFuture<HolderLookup.Provider> providerForge = event.getLookupProvider().thenApply(provider -> ECDatagen.makeBuilder(false).buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));

        final var gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new ECLunarEventTagsProvider(gen.getPackOutput(), EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, providerForge, true));
        gen.addProvider(event.includeServer(), new ECItemTagsProvider(gen.getPackOutput(), providerForge, CompletableFuture.completedFuture(blockTagKey -> Optional.empty())));
        gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(gen.getPackOutput(), event.getLookupProvider(), ECDatagen.makeBuilder(false), Set.of(EnhancedCelestials.MOD_ID, "minecraft")));
    }
}
