package corgitaco.enhancedcelestials;

import corgitaco.corgilib.CorgiLibFabric;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.core.ECEntities;
import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
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
