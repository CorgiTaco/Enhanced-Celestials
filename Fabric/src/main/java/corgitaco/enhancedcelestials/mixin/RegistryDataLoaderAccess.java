package corgitaco.enhancedcelestials.mixin;

import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public interface RegistryDataLoaderAccess {


    @Mutable
    @Accessor("WORLDGEN_REGISTRIES")
    static void ec_setWORLDGEN_REGISTRIES(List<RegistryDataLoader.RegistryData<?>> registryData) {
        throw new Error("Mixin did not apply!");
    }
}
