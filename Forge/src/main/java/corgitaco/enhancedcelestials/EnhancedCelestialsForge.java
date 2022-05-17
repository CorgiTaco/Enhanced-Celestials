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
        // This only exists to ensure classloading
        ECSounds.SOUNDS.getModId();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        EnhancedCelestials.commonSetup();
        ForgeNetworkHandler.init();
    }
}