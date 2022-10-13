package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.mixin.access.StructuresAccess;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterStructure;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;
import java.util.function.Supplier;

public class ECStructures {

    public static final RegistrationProvider<Structure> PROVIDER = RegistrationProvider.get(BuiltinRegistries.STRUCTURES, EnhancedCelestials.MOD_ID);


    public static final Holder<Structure> CRATER = register("crater", () -> new CraterStructure(structure(BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));


    private static Holder<Structure> register(String id, Supplier<Structure> structureSupplier) {
        return PROVIDER.register(id, structureSupplier).asHolder();
    }

    private static Structure.StructureSettings structure(TagKey<Biome> tag, TerrainAdjustment adj) {
        return StructuresAccess.structure(tag, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, adj);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> tag, GenerationStep.Decoration decoration, TerrainAdjustment adj) {
        return StructuresAccess.structure(tag, Map.of(), decoration, adj);
    }

    public static void loadClass() {
    }

}
