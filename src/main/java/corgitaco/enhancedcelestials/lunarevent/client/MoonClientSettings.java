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
        }), SoundEvent.CODEC.optionalFieldOf("soundTrack").orElse(Optional.empty()).forGetter((clientSettings -> {
            return clientSettings.getSoundTrack() == null ? Optional.empty() : Optional.of(clientSettings.getSoundTrack());
        }))).apply(builder, ((colorSettings, soundEvent) -> {
            return new MoonClientSettings(colorSettings, soundEvent.orElse(null));
        }));
    });

    public MoonClientSettings(ColorSettings colorSettings) {
        this(colorSettings, null);
    }

    public MoonClientSettings(ColorSettings colorSettings, SoundEvent soundEvent) {
        super(colorSettings, new ResourceLocation("textures/environment/moon_phases.png"), soundEvent);
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
