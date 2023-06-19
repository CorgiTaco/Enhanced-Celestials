package corgitaco.enhancedcelestials;

import corgitaco.corgilib.CorgiLibFabric;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.core.ECEntities;
import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.mixin.RegistryDataLoaderAccess;
import corgitaco.enhancedcelestials.mixin.RegistrySynchronizationAccess;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CorgiLibFabric.initializeCorgiLib("Enhanced Celestials Fabric Mod Initializer");
        ECRegistries.loadClasses();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();


        List<RegistryDataLoader.RegistryData<?>> registryData = new ArrayList<>(RegistryDataLoader.WORLDGEN_REGISTRIES);


        registryData.add(new RegistryDataLoader.RegistryData<>(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, LunarEvent.DIRECT_CODEC));
        registryData.add(new RegistryDataLoader.RegistryData<>(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, LunarDimensionSettings.CODEC));

        RegistryDataLoaderAccess.ec_setWORLDGEN_REGISTRIES(registryData);

        Reference2ObjectMap<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> map = new Reference2ObjectOpenHashMap<>(RegistrySynchronizationAccess.ec_getNETWORKABLE_REGISTRIES());

        map.put(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, new RegistrySynchronization.NetworkedRegistryData<>(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, LunarEvent.DIRECT_CODEC));
        map.put(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, new RegistrySynchronization.NetworkedRegistryData<>(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, LunarDimensionSettings.CODEC));
        RegistrySynchronizationAccess.ec_setNETWORKABLE_REGISTRIES(map);

        if (EnhancedCelestials.NEW_CONTENT) {
            ECEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
        }
    }
}
