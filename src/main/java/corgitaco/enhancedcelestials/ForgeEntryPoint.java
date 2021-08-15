package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECSounds;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEntryPoint {

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        Main.LOGGER.debug(Main.MOD_ID + ": Registering sounds...");
        ECSounds.SOUNDS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
        Main.LOGGER.info(Main.MOD_ID + ": Sounds registered!");
    }
}
