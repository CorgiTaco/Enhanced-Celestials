package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;
import java.util.Map;

public class ECStructureSets {
    public static final Map<ResourceKey<StructureSet>, StructureSetFactory> STRUCTURE_SET_FACTORIES = new Reference2ObjectOpenHashMap<>();


    public static final ResourceKey<StructureSet> VOLCANOES = register("volcanoes", (context) -> new StructureSet(List.of(StructureSet.entry(context.getOrThrow(ECStructures.CRATER))), new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 98778678)));

    private static ResourceKey<StructureSet> register(String id, StructureSetFactory factory) {
        ResourceKey<StructureSet> structureSetResourceKey = ResourceKey.create(Registries.STRUCTURE_SET, EnhancedCelestials.createLocation(id));
        STRUCTURE_SET_FACTORIES.put(structureSetResourceKey, factory);
        return structureSetResourceKey;
    }

    public static void bootStrap() {
        ECStructures.loadClass();
    }

    @FunctionalInterface
    public interface StructureSetFactory {

        StructureSet generate(HolderGetter<Structure> placedFeatureHolderGetter);
    }
}
