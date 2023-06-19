package corgitaco.enhancedcelestials.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RegistrySynchronization.class)
public interface RegistrySynchronizationAccess {

    @Mutable
    @Accessor("NETWORKABLE_REGISTRIES")
    static void ec_setNETWORKABLE_REGISTRIES(Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> resourceKeyNetworkedRegistryDataMap) {
        throw new Error("Mixin did not apply!");
    }

    @Accessor("NETWORKABLE_REGISTRIES")
    static Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> ec_getNETWORKABLE_REGISTRIES() {
        throw new Error("Mixin did not apply!");
    }
}
