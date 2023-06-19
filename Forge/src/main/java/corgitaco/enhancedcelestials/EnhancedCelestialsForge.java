package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECRegistries;
import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.network.ForgeNetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestialsForge {

    public EnhancedCelestialsForge() {
        // This only exists to ensure classloading
        ECSounds.SOUNDS.getModId();
        ECRegistries.loadClasses();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
        ForgeNetworkHandler.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }
}