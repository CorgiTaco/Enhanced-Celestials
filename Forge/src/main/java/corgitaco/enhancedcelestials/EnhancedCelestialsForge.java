package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.network.ForgeNetworkHandler;
import corgitaco.enhancedcelestials.util.ECRegistryObject;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.function.Supplier;

@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestialsForge {

    public EnhancedCelestialsForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bootStrap(bus);
    }

    private static void bootStrap(IEventBus eventBus) {
        register(SoundEvent.class, eventBus, () -> ECSounds.bootStrap());
    }

    @SuppressWarnings("rawtypes")
    private static <T extends IForgeRegistryEntry<T>> void register(Class clazz, IEventBus eventBus, Supplier<Collection<ECRegistryObject<T>>> registryObjectsSupplier) {
        eventBus.addGenericListener(clazz, (RegistryEvent.Register<T> event) -> {
            Collection<ECRegistryObject<T>> registryObjects = registryObjectsSupplier.get();
            IForgeRegistry<T> registry = event.getRegistry();
            for (ECRegistryObject<T> registryObject : registryObjects) {
                registryObject.object().setRegistryName(EnhancedCelestials.createLocation(registryObject.id()));
                registry.register(registryObject.object());
            }
            EnhancedCelestials.LOGGER.info("Enhanced Celestials registered: " + registry.getRegistryName());
        });
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
        ForgeNetworkHandler.init();
    }
}