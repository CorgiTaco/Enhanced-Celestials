package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.providers.ECItemTagsProvider;
import corgitaco.enhancedcelestials.providers.ECLunarEventTagsProvider;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Paths;
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
        PackOutput forgeOut = new PackOutput(Paths.get(System.getProperty("forgeoutput", null)));
        PackOutput fabricOut = new PackOutput(Paths.get(System.getProperty("fabricoutput", null)));

        CompletableFuture<HolderLookup.Provider> providerFabric = event.getLookupProvider().thenApply(provider -> makeBuilder(true).buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));
        CompletableFuture<HolderLookup.Provider> providerForge = event.getLookupProvider().thenApply(provider -> makeBuilder(false).buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));

        final var gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new ECLunarEventTagsProvider(forgeOut, false, EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, providerForge, EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), new ECLunarEventTagsProvider(fabricOut, true, ResourceKey.createRegistryKey(new ResourceLocation("lunar/event")), providerFabric, EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));

        gen.addProvider(event.includeServer(), new ECItemTagsProvider(gen.getPackOutput(), providerForge, CompletableFuture.completedFuture(blockTagKey -> Optional.empty()), EnhancedCelestials.MOD_ID, event.getExistingFileHelper()));

        class ForgeDump extends DatapackBuiltinEntriesProvider {

            public ForgeDump(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
                super(output, registries, datapackEntriesBuilder, modIds);
            }

            @Override
            public String getName() {
                return "Forge " +  super.getName();
            }
        }


        gen.addProvider(event.includeServer(), new ForgeDump(forgeOut, event.getLookupProvider(), makeBuilder(false), Set.of(EnhancedCelestials.MOD_ID, "minecraft")));
        gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(fabricOut, event.getLookupProvider(), makeBuilder(true), Set.of(EnhancedCelestials.MOD_ID, "minecraft")));
    }
}
