package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;
import java.util.function.Supplier;

public class ECStructureSets {
    public static final RegistrationProvider<StructureSet> PROVIDER = RegistrationProvider.get(BuiltinRegistries.STRUCTURE_SETS, EnhancedCelestials.MOD_ID);


    public static final Holder<StructureSet> VOLCANOES = register("volcanoes", () -> new StructureSet(List.of(StructureSet.entry(ECStructures.CRATER)), new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 98778678)));

    private static <T extends StructureSet> Holder<T> register(String id, Supplier<? extends T> set) {
        return PROVIDER.<T>register(id, set).asHolder();
    }

    public static void bootStrap() {
        ECStructures.loadClass();
    }
}
