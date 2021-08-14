package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(Registry.class)
public interface RegistryAccess {

    @Invoker
    static <T> Registry<T> invokeCreateRegistry(RegistryKey<? extends Registry<T>> registryKey, Supplier<T> supplier) {
        throw new Error("Mixin did not apply!");
    }
}