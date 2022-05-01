package corgitaco.enhancedcelestials.api.lunarevent.client;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;

import java.util.function.Function;

public abstract class LunarEventClientSettings {

    public static final Codec<LunarEventClientSettings> CODEC = EnhancedCelestialsRegistry.LUNAR_CLIENT_EVENT_SETTINGS.byNameCodec().dispatchStable(LunarEventClientSettings::codec, Function.identity());

    private final ColorSettings colorSettings;
    private final float moonSize;
    private final ResourceLocation moonTextureLocation;
    @Nullable
    private final SoundEvent soundTrack;

    public LunarEventClientSettings(ColorSettings colorSettings, float moonSize, ResourceLocation moonTextureLocation, @Nullable SoundEvent soundTrack) {
        this.colorSettings = colorSettings;
        this.moonSize = moonSize;
        this.moonTextureLocation = moonTextureLocation;
        this.soundTrack = soundTrack;
    }

    public abstract Codec<? extends LunarEventClientSettings> codec();

    public abstract LunarEventClient<?> createClient();

    public ColorSettings getColorSettings() {
        return colorSettings;
    }

    public ResourceLocation getMoonTextureLocation() {
        return moonTextureLocation;
    }

    @Nullable
    public SoundEvent getSoundTrack() {
        return soundTrack;
    }

    public float getMoonSize() {
        return moonSize;
    }
}
