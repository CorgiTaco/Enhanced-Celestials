package corgitaco.enhancedcelestials;

import corgitaco.corgilib.CorgiLibFabric;
import corgitaco.enhancedcelestials.core.ECEntities;
import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CorgiLibFabric.initializeCorgiLib("Enhanced Celestials Fabric Mod Initializer");
        ECRegistries.loadClasses();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();

        if (EnhancedCelestials.NEW_CONTENT) {
            ECEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
        }
    }
}
