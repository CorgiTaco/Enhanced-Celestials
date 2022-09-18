package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECEntities;
import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ECRegistries.loadClasses();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();

        ECEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
    }
}
