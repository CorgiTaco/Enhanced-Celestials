package dev.corgitaco.enhancedcelestials.fabric.datagen.provider;

import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ECFabricDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    private final String generatorName;

    public ECFabricDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String generatorName) {
        super(output, registriesFuture);
        this.generatorName = generatorName;
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY));
        entries.addAll(registries.lookupOrThrow(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY));
    }

    @Override
    public String getName() {
        return this.generatorName;
    }
}
