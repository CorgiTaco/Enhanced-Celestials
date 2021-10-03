package corgitaco.enhancedcelestials.api.lunarevent.client;

import com.mojang.blaze3d.vertex.PoseStack;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.client.SkyRenderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;

public abstract class LunarEventClient<T extends LunarEventClientSettings> implements SkyRenderable {

    private final ColorSettings colorSettings;
    private final float moonSize;
    private final ResourceLocation moonTextureLocation;
    @Nullable
    private final SoundEvent soundTrack;

    public LunarEventClient(T clientSettings) {
        this.colorSettings = clientSettings.getColorSettings();
        this.moonSize = clientSettings.getMoonSize();
        this.moonTextureLocation = clientSettings.getMoonTextureLocation();
        this.soundTrack = clientSettings.getSoundTrack();
    }

    @Override
    public void renderSky(PoseStack matrixStackIn, float partialTicks) {

    }

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
