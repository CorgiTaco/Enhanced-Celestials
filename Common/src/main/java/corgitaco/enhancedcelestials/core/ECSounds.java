package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import corgitaco.enhancedcelestials.util.ECRegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ECSounds {

    public static final RegistrationProvider<SoundEvent> SOUNDS = RegistrationProvider.get(Registry.SOUND_EVENT, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<SoundEvent> BLOOD_MOON = createSound("blood_moon");
    public static final RegistryObject<SoundEvent> BLUE_MOON = createSound("blue_moon");
    public static final RegistryObject<SoundEvent> HARVEST_MOON = createSound("harvest_moon");

    public static RegistryObject<SoundEvent> createSound(String location) {
        final ResourceLocation soundLocation = new ResourceLocation(EnhancedCelestials.MOD_ID, location);
        return SOUNDS.register(location, () -> new SoundEvent(soundLocation));
    }

}
