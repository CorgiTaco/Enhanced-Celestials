package dev.corgitaco.enhancedcelestials.core;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.ECItemTags;
import dev.corgitaco.enhancedcelestials.api.ECLunarEventTags;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import dev.corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructurePieceTypes;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureSets;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureTypes;

public record ECRegistries() {

    public static void loadClasses() {
        ECSounds.loadClass();
        EnhancedCelestialsRegistry.init();
        if (EnhancedCelestials.NEW_CONTENT) {
            ECBlocks.classLoad();
            ECItems.classLoad();
            ECEntities.loadClass();
            ECStructureTypes.loadClass();
            ECStructurePieceTypes.bootStrap();
            ECStructureSets.bootStrap();
        }
        DefaultLunarEvents.loadClass();
        DefaultLunarDimensionSettings.loadClass();
        ECLunarEventTags.loadClass();
        ECItemTags.loadClass();
    }
}
