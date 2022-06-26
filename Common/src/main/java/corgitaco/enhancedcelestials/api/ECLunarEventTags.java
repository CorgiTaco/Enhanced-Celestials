package corgitaco.enhancedcelestials.api;

import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ECLunarEventTags {

    public static final TagKey<LunarEvent> BLOOD_MOON = createEventTag("blood_moon");
    public static final TagKey<LunarEvent> BLUE_MOON = createEventTag("blue_moon");
    public static final TagKey<LunarEvent> HARVEST_MOON = createEventTag("harvest_moon");
    public static final TagKey<LunarEvent> SUPER_MOON = createEventTag("super_moon");

    private static TagKey<LunarEvent> createEventTag(String id) {
        return TagKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, new ResourceLocation(EnhancedCelestialsRegistry.MOD_ID, id));
    }

    public static void loadClass() {
    }
}