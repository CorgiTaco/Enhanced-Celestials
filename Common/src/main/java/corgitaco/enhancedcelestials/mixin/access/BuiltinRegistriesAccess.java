package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BuiltinRegistries.class)
public interface BuiltinRegistriesAccess {

    @Accessor("ACCESS") @Mutable
    static void ec_setRegistryAccess(RegistryAccess registryAccess) {
        throw new Error("Mixin did not apply!!");
    }

    @Accessor("WRITABLE_REGISTRY") @Mutable
    static WritableRegistry<WritableRegistry<?>> ec_getWRITABLE_REGISTRY() {
        throw new Error("Mixin did not apply!!");
    }

    @Invoker("registerSimple")
    static <T> Registry<T> ec_invokeRegisterSimple(ResourceKey<? extends Registry<T>> $$0, BuiltinRegistries.RegistryBootstrap<T> $$1) {
        throw new Error("Mixin did not apply!!");
    }
}
