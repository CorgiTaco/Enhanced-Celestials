package dev.corgitaco.enhancedcelestials.neoforge.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.platform.services.RegistrationService;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AutoService(RegistrationService.class)
public class NeoForgeRegistrationService implements RegistrationService {

    public static final List<Consumer<DataPackRegistryEvent.NewRegistry>> DATAPACK_REGISTRIES = new ArrayList<>();

    public static final Map<ResourceKey, DeferredRegister> CACHED = new Reference2ObjectOpenHashMap<>();


    @Override
    public <T> Registry<T> createSimpleBuiltin(ResourceKey<Registry<T>> registryKey) {
       return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    @Override
    public <T> Supplier<T> register(Registry<T> registry, String location, Supplier<T> value) {
        return CACHED.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), EnhancedCelestials.MOD_ID)).register(location, value);
    }

    @Override
    public <T> void registerDatapackRegistry(ResourceKey<Registry<T>> key, Supplier<Codec<T>> codec) {
        DATAPACK_REGISTRIES.add(newRegistry -> newRegistry.dataPackRegistry(key, codec.get()));
    }
}