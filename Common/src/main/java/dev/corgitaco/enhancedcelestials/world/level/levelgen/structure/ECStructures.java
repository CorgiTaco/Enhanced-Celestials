package dev.corgitaco.enhancedcelestials.world.level.levelgen.structure;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterStructure;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class ECStructures {

    public static final Map<ResourceKey<Structure>, StructureFactory> STRUCTURE_FACTORIES = new Reference2ObjectOpenHashMap<>();


    public static final ResourceKey<Structure> CRATER = register("crater", (context) -> new CraterStructure(structure(context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));


    private static ResourceKey<Structure> register(String id, StructureFactory factory) {
        ResourceKey<Structure> structureSetResourceKey = ResourceKey.create(Registries.STRUCTURE, EnhancedCelestials.createLocation(id));
        STRUCTURE_FACTORIES.put(structureSetResourceKey, factory);
        return structureSetResourceKey;
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> tag, TerrainAdjustment adj) {
        return new Structure.StructureSettings(tag, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, adj);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> tag, Map<MobCategory, StructureSpawnOverride> spawnOverrides, TerrainAdjustment adj) {
        return new Structure.StructureSettings(tag, spawnOverrides, GenerationStep.Decoration.SURFACE_STRUCTURES, adj);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> tag, GenerationStep.Decoration decoration, TerrainAdjustment adj) {
        return new Structure.StructureSettings(tag, Map.of(), decoration, adj);
    }
    public static void loadClass() {
    }

    @FunctionalInterface
    public interface StructureFactory {
        Structure generate(BootstrapContext<Structure> structureFactoryBootstapContext);
    }
}
