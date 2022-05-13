package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.network.FabricNetworkHandler;
import corgitaco.enhancedcelestials.util.ECRegistryObject;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;

import java.util.Collection;

import static corgitaco.enhancedcelestials.EnhancedCelestials.createLocation;

public class EnhancedCelestialsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();
    }
}
