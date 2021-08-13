package corgitaco.enhancedcelestials.lunarevent.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClient;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.lunarevent.client.settings.MoonClient;
import net.minecraft.util.ResourceLocation;

public class MoonClientSettings extends LunarEventClientSettings {

    public static final Codec<MoonClientSettings> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(ColorSettings.CODEC.fieldOf("colorSettings").forGetter((moonClientSettings) -> {
            return moonClientSettings.getColorSettings();
        })).apply(builder, MoonClientSettings::new);
    });

    public MoonClientSettings(ColorSettings colorSettings) {
        super(colorSettings, new ResourceLocation("textures/environment/moon_phases.png"), null);
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
