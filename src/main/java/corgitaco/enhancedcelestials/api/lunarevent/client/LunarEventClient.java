package corgitaco.enhancedcelestials.api.lunarevent.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.client.SkyRenderable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
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
    public void renderSky(MatrixStack matrixStackIn, float partialTicks) {

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
