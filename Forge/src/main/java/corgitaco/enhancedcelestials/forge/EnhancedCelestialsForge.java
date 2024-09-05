package corgitaco.enhancedcelestials.forge;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.forge.network.ForgeNetworkHandler;
import corgitaco.enhancedcelestials.forge.platform.ForgeRegistrationService;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestialsForge {

    public EnhancedCelestialsForge() {
        ECRegistries.loadClasses();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.<DataPackRegistryEvent.NewRegistry>addListener(event -> ForgeRegistrationService.DATAPACK_REGISTRIES.forEach(newRegistryConsumer -> newRegistryConsumer.accept(event)));
        ForgeRegistrationService.CACHED.values().forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
        ForgeNetworkHandler.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }
}