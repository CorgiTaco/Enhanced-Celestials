package dev.corgitaco.enhancedcelestials.core;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.platform.services.RegistrationService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ECSounds {

    public static final Supplier<SoundEvent> BLOOD_MOON = createSound("blood_moon");
    public static final Supplier<SoundEvent> BLUE_MOON = createSound("blue_moon");
    public static final Supplier<SoundEvent> HARVEST_MOON = createSound("harvest_moon");

    public static Supplier<SoundEvent> createSound(String location) {
        final ResourceLocation soundLocation = EnhancedCelestials.createLocation(location);
        return RegistrationService.INSTANCE.register(BuiltInRegistries.SOUND_EVENT, location, () -> SoundEvent.createVariableRangeEvent(soundLocation));
    }

    public static void loadClass() {}
}
