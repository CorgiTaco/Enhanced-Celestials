package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.Main;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

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
        Registry.register(Registry.SOUND_EVENT, soundLocation, soundEvent);
        SOUNDS.add(soundEvent);
        return soundEvent;
    }
}
