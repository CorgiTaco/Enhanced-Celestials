package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class ECSounds {

    public static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static final SoundEvent BLOOD_MOON = createSound("blood_moon");
    public static final SoundEvent BLUE_MOON = createSound("blue_moon");
    public static final SoundEvent HARVEST_MOON = createSound("harvest_moon");

    public static SoundEvent createSound(String location) {
        ResourceLocation soundLocation = new ResourceLocation(Main.MOD_ID, location);
        SoundEvent soundEvent = new SoundEvent(soundLocation);
        soundEvent.setRegistryName(soundLocation);
        SOUNDS.add(soundEvent);
        return soundEvent;
    }
}
