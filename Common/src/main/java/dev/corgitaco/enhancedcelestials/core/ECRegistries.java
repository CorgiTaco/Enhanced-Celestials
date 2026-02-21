package dev.corgitaco.enhancedcelestials.core;

import dev.corgitaco.enhancedcelestials.api.ECItemTags;
import dev.corgitaco.enhancedcelestials.api.ECLunarEventTags;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;

public record ECRegistries() {

    public static void loadClasses() {
        ECSounds.loadClass();
        EnhancedCelestialsRegistry.init();
        DefaultLunarEvents.loadClass();
        DefaultLunarDimensionSettings.loadClass();
        ECLunarEventTags.loadClass();
        ECItemTags.loadClass();
    }
}
