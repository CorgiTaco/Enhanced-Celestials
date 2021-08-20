package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.core.ECSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

public class FabricEntryPoint implements ModInitializer {

    public static void registerSounds() {
        Main.LOGGER.debug(Main.MOD_ID + ": Registering sounds...");
        List<SoundEvent> sounds = ECSounds.SOUNDS;
        Main.LOGGER.info(Main.MOD_ID + ": Sounds registered!");
    }

    @Override
    public void onInitialize() {
        registerSounds();
        Main.commonSetup();
    }
}
