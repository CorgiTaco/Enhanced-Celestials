package corgitaco.enhancedcelestials.lunarevent.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClient;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.lunarevent.client.settings.MoonClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.Optional;

public class MoonClientSettings extends LunarEventClientSettings {

    public static final Codec<MoonClientSettings> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(ColorSettings.CODEC.fieldOf("colorSettings").forGetter((moonClientSettings) -> {
            return moonClientSettings.getColorSettings();
        }), Codec.FLOAT.fieldOf("moonSize").orElse(20.0F).forGetter((clientSettings) -> {
            return clientSettings.getMoonSize();
        }), ResourceLocation.CODEC.fieldOf("moonTextureLocation").orElse(new ResourceLocation("textures/environment/moon_phases.png")).forGetter((clientSettings) -> {
            return clientSettings.getMoonTextureLocation();
        }), SoundEvent.CODEC.optionalFieldOf("soundTrack").orElse(Optional.empty()).forGetter((clientSettings) -> {
            return clientSettings.getSoundTrack() == null ? Optional.empty() : Optional.of(clientSettings.getSoundTrack());
        })).apply(builder, ((colorSettings, moonSize, moonTextureLocation, soundEvent) -> {
            return new MoonClientSettings(colorSettings, moonSize, moonTextureLocation, soundEvent.orElse(null));
        }));
    });

    public MoonClientSettings(ColorSettings colorSettings) {
        this(colorSettings, 20.0F, null);
    }

    public MoonClientSettings(ColorSettings colorSettings, float moonSize, SoundEvent soundEvent) {
        this(colorSettings, moonSize, new ResourceLocation("textures/environment/moon_phases.png"), soundEvent);
    }

    public MoonClientSettings(ColorSettings colorSettings, float moonSize, ResourceLocation moonTextureLocation, SoundEvent soundEvent) {
        super(colorSettings, moonSize, moonTextureLocation, soundEvent);
    }

    @Override
    public Codec<? extends LunarEventClientSettings> codec() {
        return CODEC;
    }

    @Override
    public LunarEventClient<?> createClient() {
        return new MoonClient(this);
    }
}
