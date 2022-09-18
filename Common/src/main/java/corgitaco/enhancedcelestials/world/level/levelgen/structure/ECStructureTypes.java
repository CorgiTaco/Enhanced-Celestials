package corgitaco.enhancedcelestials.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.crater.CraterStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class ECStructureTypes {

    public static final RegistrationProvider<StructureType<?>> PROVIDER = RegistrationProvider.get(Registry.STRUCTURE_TYPES, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<StructureType<CraterStructure>> CRATER = register("crater", () -> CraterStructure.CODEC);

    private static <S extends Structure> RegistryObject<StructureType<S>> register(String id, Supplier<? extends Codec<S>> codec) {
        return PROVIDER.register(id, () -> codec::get);
    }

    public static void loadClass() {
    }
}
