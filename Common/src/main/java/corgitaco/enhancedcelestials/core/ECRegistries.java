package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestialsBlocks;
import corgitaco.enhancedcelestials.EnhancedCelestialsItems;
import corgitaco.enhancedcelestials.api.ECItemTags;
import corgitaco.enhancedcelestials.api.ECLunarEventTags;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsBuiltinRegistries;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.entityfilter.EntityFilter;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;

public record ECRegistries() {

    public static void loadClasses() {
        EnhancedCelestialsRegistry.init();
        EnhancedCelestialsBuiltinRegistries.init();
        EntityFilter.init();
        DefaultLunarEvents.loadClass();
        DefaultLunarDimensionSettings.loadClass();
        ECLunarEventTags.loadClass();
        ECItemTags.loadClass();

        EnhancedCelestialsBlocks.classLoad();
        EnhancedCelestialsItems.classLoad();
    }
}
