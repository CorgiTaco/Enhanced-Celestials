package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();
    }
}
