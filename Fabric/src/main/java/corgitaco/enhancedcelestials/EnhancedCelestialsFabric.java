package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ECRegistries.loadClasses();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();
    }
}
