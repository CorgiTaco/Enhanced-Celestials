package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.network.NetworkHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class ForgeEntryPoint {

    public ForgeEntryPoint() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent commonSetupEvent) {
        NetworkHandler.init();
        Main.commonSetup();
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        Main.LOGGER.debug(Main.MOD_ID + ": Registering sounds...");
        ECSounds.SOUNDS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
        Main.LOGGER.info(Main.MOD_ID + ": Sounds registered!");
    }
}
