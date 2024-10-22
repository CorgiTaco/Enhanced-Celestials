package dev.corgitaco.enhancedcelestials.forge;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.core.ECRegistries;
import dev.corgitaco.enhancedcelestials.forge.network.ForgeNetworkHandler;
import dev.corgitaco.enhancedcelestials.forge.platform.ForgeRegistrationService;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestialsForge {
    public EnhancedCelestialsForge(final FMLJavaModLoadingContext context) {
        ECRegistries.loadClasses();
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.<DataPackRegistryEvent.NewRegistry>addListener(event -> ForgeRegistrationService.DATAPACK_REGISTRIES.forEach(newRegistryConsumer -> newRegistryConsumer.accept(event)));
        ForgeRegistrationService.CACHED.values().forEach(deferredRegister -> deferredRegister.register(bus));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
        ForgeNetworkHandler.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }
}