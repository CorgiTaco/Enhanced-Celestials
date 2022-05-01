package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.util.ECRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ECSounds {

    public static final List<ECRegistryObject<SoundEvent>> SOUNDS = new ArrayList<>();

    public static final SoundEvent BLOOD_MOON = createSound("blood_moon");
    public static final SoundEvent BLUE_MOON = createSound("blue_moon");
    public static final SoundEvent HARVEST_MOON = createSound("harvest_moon");

    public static SoundEvent createSound(String location) {
        ResourceLocation soundLocation = new ResourceLocation(EnhancedCelestials.MOD_ID, location);
        SoundEvent soundEvent = new SoundEvent(soundLocation);
        SOUNDS.add(new ECRegistryObject<>(soundEvent, location));
        return soundEvent;
    }

    public static Collection<ECRegistryObject<SoundEvent>> bootStrap() {
        return SOUNDS;
    }

}
