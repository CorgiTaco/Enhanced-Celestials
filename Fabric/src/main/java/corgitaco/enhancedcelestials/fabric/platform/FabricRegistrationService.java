package corgitaco.enhancedcelestials.fabric.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.platform.services.RegistrationService;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

@AutoService(RegistrationService.class)
public class FabricRegistrationService implements RegistrationService {


    @Override
    public <T> Registry<T> createSimpleBuiltin(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }

    @Override
    public <T> Supplier<T> register(Registry<T> registry, String name, Supplier<T> value) {
        T register = Registry.register(registry, EnhancedCelestials.createLocation(name), value.get());
        return () -> register;
    }

    @Override
    public <T> void registerDatapackRegistry(ResourceKey<Registry<T>> key, Supplier<Codec<T>> codec) {
        DynamicRegistries.registerSynced(key, codec.get());
    }
}