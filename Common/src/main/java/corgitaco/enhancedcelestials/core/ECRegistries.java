package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.ECItemTags;
import corgitaco.enhancedcelestials.api.ECLunarEventTags;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.DefaultLunarEvents;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructurePieceTypes;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureSets;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructureTypes;

public record ECRegistries() {

    public static void loadClasses() {
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
