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
        registryBootStrap();
        EnhancedCelestials.commonSetup();
        FabricNetworkHandler.init();
    }

    private static void registryBootStrap() {
        register(Registry.SOUND_EVENT, ECSounds.bootStrap());
    }


    public static <T> void register(Registry<T> registry, Collection<ECRegistryObject<T>> objects) {
        for (ECRegistryObject<T> object : objects) {
            Registry.register(registry, createLocation(object.id()), object.object());
        }
        EnhancedCelestials.LOGGER.info("Enhanced Celestials registered: " + registry.toString());
    }
}
