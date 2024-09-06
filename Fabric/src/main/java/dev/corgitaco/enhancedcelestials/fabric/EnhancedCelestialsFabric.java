package dev.corgitaco.enhancedcelestials.fabric;

import corgitaco.corgilib.fabric.CorgiLibFabric;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import dev.corgitaco.enhancedcelestials.core.ECEntities;
import dev.corgitaco.enhancedcelestials.core.ECRegistries;
import dev.corgitaco.enhancedcelestials.fabric.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CorgiLibFabric.initializeCorgiLib("Enhanced Celestials Fabric Mod Initializer");
        ECRegistries.loadClasses();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();

        DynamicRegistries.registerSynced(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, LunarEvent.DIRECT_CODEC);
        DynamicRegistries.registerSynced(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, LunarDimensionSettings.CODEC);

        if (EnhancedCelestials.NEW_CONTENT) {
            ECEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
        }
    }
}
