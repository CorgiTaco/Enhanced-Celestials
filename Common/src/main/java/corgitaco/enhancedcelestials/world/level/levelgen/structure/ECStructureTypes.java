package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import corgitaco.enhancedcelestials.platform.services.RegistrationService;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Locale;
import java.util.function.Supplier;

public class ECStructureTypes {


    public static final Supplier<StructureType<CraterStructure>> CRATER = register("crater", () -> () -> CraterStructure.CODEC);

    private static <S extends Structure, ST extends StructureType<S>> Supplier<ST> register(String id, Supplier<ST> type) {
        return RegistrationService.INSTANCE.register((Registry<ST>) BuiltInRegistries.STRUCTURE_TYPE, id.toLowerCase(Locale.ROOT), type);
    }

    public static void loadClass() {
    }
}
