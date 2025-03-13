package dev.corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;

public record LunarEventProbabilities(int priority,
                                      Map<ResourceKey<LunarEvent>, Map<ResourceKey<Level>, LunarEvent.SpawnRequirements>> probabilitiesByEvent) {

    public static Codec<LunarEventProbabilities> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("priority").forGetter(LunarEventProbabilities::priority),
                    Codec.unboundedMap(ResourceKey.codec(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY), Codec.unboundedMap(ResourceKey.codec(Registries.DIMENSION), LunarEvent.SpawnRequirements.CODEC)).fieldOf("dimension_probabilities").forGetter(LunarEventProbabilities::probabilitiesByEvent)
            ).apply(instance, LunarEventProbabilities::new)
    );
}
