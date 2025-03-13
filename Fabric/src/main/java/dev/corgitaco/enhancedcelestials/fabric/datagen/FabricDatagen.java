package dev.corgitaco.enhancedcelestials.fabric.datagen;

import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.datagen.ECDatagen;
import dev.corgitaco.enhancedcelestials.datagen.providers.ECItemTagsProvider;
import dev.corgitaco.enhancedcelestials.datagen.providers.ECLunarEventTagsProvider;
import dev.corgitaco.enhancedcelestials.fabric.datagen.provider.ECFabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.registries.RegistriesDatapackGenerator;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FabricDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();


        pack.addProvider((output, registriesFuture) ->
                new ECLunarEventTagsProvider(output, EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, registriesFuture, false)
        );

        pack.addProvider((output, registriesFuture) ->
                new ECItemTagsProvider(output, registriesFuture, CompletableFuture.completedFuture(blockTagKey -> Optional.empty()))
        );
        pack.addProvider((output, registriesFuture) -> new ECFabricDynamicRegistryProvider(output, registriesFuture, "Enhanced Celestials Lunar Registries"));
        pack.addProvider((output, registriesFuture) -> {
            FabricDataOutput minecraft = new FabricDataOutput(FabricLoader.getInstance().getModContainer("minecraft").orElseThrow(), output.getOutputFolder(), false);
            return new ECFabricDynamicRegistryProvider(minecraft, registriesFuture, "Minecraft Lunar Registries");
        });
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        ECDatagen.makeBuilder(false, registryBuilder);
    }
}