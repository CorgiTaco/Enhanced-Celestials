package corgitaco.enhancedcelestials.platform.services;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistrationService {

    RegistrationService INSTANCE = Services.load(RegistrationService.class);


    <T> Registry<T> createSimpleBuiltin(ResourceKey<Registry<T>> registryKey);


    <T> Supplier<T> register(Registry<T> registry, String name, Supplier<T> value);


    <T> void registerDatapackRegistry(ResourceKey<Registry<T>> key, Supplier<Codec<T>> codec);
}