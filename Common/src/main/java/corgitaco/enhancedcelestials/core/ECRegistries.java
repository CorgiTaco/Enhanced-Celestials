package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.api.ECLunarEventTags;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsBuiltinRegistries;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;

public record ECRegistries() {

    public static void loadClasses(){
        EnhancedCelestialsRegistry.init();
        EnhancedCelestialsBuiltinRegistries.init();
        DefaultLunarEvents.loadClass();
        DefaultLunarDimensionSettings.loadClass();
        ECLunarEventTags.loadClass();
    }
}
