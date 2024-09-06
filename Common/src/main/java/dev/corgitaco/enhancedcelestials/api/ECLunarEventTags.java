package dev.corgitaco.enhancedcelestials.api;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.tags.TagKey;

public class ECLunarEventTags {

    public static final TagKey<LunarEvent> BLOOD_MOON = createEventTag("blood_moon");
    public static final TagKey<LunarEvent> BLUE_MOON = createEventTag("blue_moon");
    public static final TagKey<LunarEvent> HARVEST_MOON = createEventTag("harvest_moon");
    public static final TagKey<LunarEvent> SUPER_MOON = createEventTag("super_moon");

    private static TagKey<LunarEvent> createEventTag(String id) {
        return TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation(id));
    }

    public static void loadClass() {
    }
}