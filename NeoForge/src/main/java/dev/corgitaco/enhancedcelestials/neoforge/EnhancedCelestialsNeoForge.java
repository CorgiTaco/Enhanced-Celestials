package dev.corgitaco.enhancedcelestials.neoforge;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.core.ECRegistries;
import dev.corgitaco.enhancedcelestials.neoforge.platform.NeoForgeRegistrationService;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * Main class for the mod on the NeoForge platform.
 */
@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestialsNeoForge {
    public EnhancedCelestialsNeoForge(final IEventBus bus) {
        ECRegistries.loadClasses();
        bus.addListener(this::commonSetup);
        bus.<DataPackRegistryEvent.NewRegistry>addListener(event -> NeoForgeRegistrationService.DATAPACK_REGISTRIES.forEach(newRegistryConsumer -> newRegistryConsumer.accept(event)));
        NeoForgeRegistrationService.CACHED.values().forEach(deferredRegister -> deferredRegister.register(bus));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
    }
}
