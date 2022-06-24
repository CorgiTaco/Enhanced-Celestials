package corgitaco.enhancedcelestials.api.lunarevent.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public record LunarEventClientSettings(ColorSettings colorSettings, float moonSize,
                                        ResourceLocation moonTextureLocation, @Nullable SoundEvent soundTrack) {

    public static final Codec<LunarEventClientSettings> CODEC = RecordCodecBuilder.create((builder) ->
            builder.group(ColorSettings.CODEC.fieldOf("colorSettings").forGetter(LunarEventClientSettings::colorSettings),
                    Codec.FLOAT.fieldOf("moonSize").orElse(20.0F).forGetter(LunarEventClientSettings::moonSize),
                    ResourceLocation.CODEC.optionalFieldOf("moonTextureLocation").orElse(Optional.of(new ResourceLocation("textures/environment/moon_phases.png"))).forGetter(clientSettings -> clientSettings.moonTextureLocation() == null ? Optional.of(new ResourceLocation("textures/environment/moon_phases.png")) : Optional.of(clientSettings.moonTextureLocation())),
                    SoundEvent.CODEC.optionalFieldOf("soundTrack").orElse(Optional.empty()).forGetter((clientSettings) -> clientSettings.soundTrack() == null ? Optional.empty() : Optional.of(clientSettings.soundTrack()))
            ).apply(builder, ((colorSettings, moonSize, moonTextureLocation, soundEvent) ->
                    new LunarEventClientSettings(colorSettings, moonSize, moonTextureLocation.orElse(null), soundEvent.orElse(null)))
            )
    );
}
